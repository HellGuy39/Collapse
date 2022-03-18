package com.hellguy39.collapse.presentaton.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.IBinder
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.hellguy39.collapse.presentaton.activities.main.DescriptionAdapter
import com.hellguy39.collapse.utils.PlayerType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlayerService : Service() {

    private lateinit var playerNotificationManager: PlayerNotificationManager

    private val notificationId = 101
    private val channelId = "player_channel"
    private val channelName = "foreground_player_channel"

    //private val mediaDataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(this)

    companion object {

        private lateinit var exoPlayer: ExoPlayer

        /*private var positionLiveData = MutableLiveData<Long>()*/
        private var mediaMetadataLiveData = MutableLiveData<MediaMetadata>()
        private var isPlayingLiveData = MutableLiveData<Boolean>()

        fun startService(context: Context, contentWrapper: ServiceContentWrapper) {
            val service = Intent(context, PlayerService::class.java)
            service.putExtra("track_list", contentWrapper)
            ContextCompat.startForegroundService(context, service)
        }

        fun stopService(context: Context) {
            val service = Intent(context, PlayerService::class.java)
            context.stopService(service)
        }

        fun isPlaying(): LiveData<Boolean> = isPlayingLiveData
        fun getCurrentPosition(): Long = exoPlayer.currentPosition
        fun getCurrentMetadata(): LiveData<MediaMetadata> = mediaMetadataLiveData
        fun getDuration(): Long = exoPlayer.duration
        fun getAudioSessionId(): Int = exoPlayer.audioSessionId
        fun isShuffle(): Boolean = exoPlayer.shuffleModeEnabled
        fun isRepeat(): Int = exoPlayer.repeatMode

        //Control
        fun onPlay() = exoPlayer.play()
        fun onPause() = exoPlayer.pause()
        fun onNext() { if (exoPlayer.hasNextMediaItem()) exoPlayer.seekToNextMediaItem() }
        fun onPrevious() { if (exoPlayer.hasPreviousMediaItem()) exoPlayer.seekToPreviousMediaItem() }
        fun onShuffle(b: Boolean) { exoPlayer.shuffleModeEnabled = b }
        fun onRepeat(n: Int) = { exoPlayer.repeatMode = n }
        fun onSeekTo(pos: Long) = exoPlayer.seekTo(pos)
    }

    override fun onCreate() {
        super.onCreate()
        isPlayingLiveData.value = false
        createNotificationsChannel()

        playerNotificationManager = PlayerNotificationManager.Builder(
            this,
            notificationId,
            channelId
        ).setMediaDescriptionAdapter(DescriptionAdapter(this, resources))
            .setNotificationListener(object : PlayerNotificationManager.NotificationListener {
                override fun onNotificationPosted(
                    notificationId: Int,
                    notification: Notification,
                    ongoing: Boolean
                ) {
                    startForeground(notificationId, notification)
                }

                override fun onNotificationCancelled(
                    notificationId: Int,
                    dismissedByUser: Boolean
                ) {
                    stopSelf()
                }
            })
            .build()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val content = intent?.getSerializableExtra("track_list") as ServiceContentWrapper

        when(content.type) {
            PlayerType.LocalTrack -> initDefaultPlayer(content)
            PlayerType.Radio -> initRadioPlayer(content)
        }

        playerNotificationManager.setPlayer(exoPlayer)
        exoPlayer.playWhenReady = true

        return START_STICKY
    }

    private fun initDefaultPlayer(content: ServiceContentWrapper) {
        exoPlayer = ExoPlayer.Builder(this).build()
        val trackList = content.trackList

        if (trackList.isNullOrEmpty())
            return

        for (n in trackList.indices) {
            exoPlayer.addMediaItem(MediaItem.fromUri(trackList[n].path))
        }

        exoPlayer.seekToDefaultPosition(content.position)

        exoPlayer.addListener(object : Player.Listener {
            override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                mediaMetadataLiveData.value = mediaMetadata
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                isPlayingLiveData.value = isPlaying
            }

            override fun onAudioSessionIdChanged(audioSessionId: Int) {
                super.onAudioSessionIdChanged(audioSessionId)
            }
        })


        exoPlayer.prepare()

    }

    private fun initRadioPlayer(content: ServiceContentWrapper) {
        val mediaDataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(this)

        val mediaSource = ProgressiveMediaSource.Factory(mediaDataSourceFactory).createMediaSource(MediaItem.fromUri(
            Uri.parse(content.url)))

        val mediaSourceFactory: MediaSource.Factory = DefaultMediaSourceFactory(mediaDataSourceFactory)

        exoPlayer = ExoPlayer.Builder(this)
            .setMediaSourceFactory(mediaSourceFactory)
            .build()

        exoPlayer.addMediaSource(mediaSource)
        exoPlayer.prepare()
    }

    private fun createNotificationsChannel() {
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_LOW
        )
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(channel)
    }

    override fun onDestroy() {
        exoPlayer.release()
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        //playerNotificationManager.setPlayer(null)
        stopSelf()
    }
}
