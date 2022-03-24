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
import com.hellguy39.domain.utils.PlayerType
import com.hellguy39.domain.models.ServiceContentWrapper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlayerService : Service() {

    private lateinit var playerNotificationManager: PlayerNotificationManager

    private val notificationId = 101
    private val channelId = "player_channel"
    private val channelName = "foreground_player_channel"

    //private val mediaDataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(this)

    companion object {

        private var isRunningService = false
        private var playlistName: String? = null

        private lateinit var exoPlayer: ExoPlayer

        /*private var positionLiveData = MutableLiveData<Long>()*/
        private val mediaMetadataLiveData = MutableLiveData<MediaMetadata>()
        private val isPlayingLiveData = MutableLiveData<Boolean>()
        private val audioSessionIdLiveData = MutableLiveData<Int>()
        private val playerTypeLiveData = MutableLiveData<Enum<PlayerType>>()
        private val contentPositionLiveData = MutableLiveData<Int>()

        fun startService(context: Context, contentWrapper: ServiceContentWrapper) {

            if (isRunningService)
                stopService(context)

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
        fun getContentPosition(): LiveData<Int> = contentPositionLiveData
        fun getDuration(): Long = exoPlayer.duration
        fun getAudioSessionId(): LiveData<Int> = audioSessionIdLiveData
        fun isShuffle(): Boolean = exoPlayer.shuffleModeEnabled
        fun isRepeat(): Int = exoPlayer.repeatMode
        fun getPlaylistName(): String? = playlistName

        //Control
        fun onPlay() = exoPlayer.play()
        fun onPause() = exoPlayer.pause()
        fun onNext() { if (exoPlayer.hasNextMediaItem()) exoPlayer.seekToNextMediaItem() }
        fun onPrevious() { if (exoPlayer.hasPreviousMediaItem()) exoPlayer.seekToPreviousMediaItem() }
        fun onShuffle(b: Boolean) { exoPlayer.shuffleModeEnabled = b }
        fun onRepeat(n: Int) { exoPlayer.repeatMode = n }
        fun onSeekTo(pos: Long) = exoPlayer.seekTo(pos)
    }

    override fun onCreate() {
        super.onCreate()
        isRunningService = true

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
        val content = intent?.getParcelableExtra<ServiceContentWrapper>("track_list") as ServiceContentWrapper

        playerTypeLiveData.value = content.type
        playlistName = content.playlistName

        when(content.type) {
            PlayerType.LocalTrack -> initDefaultPlayer(content)
            PlayerType.Radio -> initRadioPlayer(content)
        }

        exoPlayer.addListener(object : Player.Listener {
            override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                mediaMetadataLiveData.value = mediaMetadata
                contentPositionLiveData.value = exoPlayer.currentMediaItemIndex
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                isPlayingLiveData.value = isPlaying
            }

            override fun onAudioSessionIdChanged(audioSessionId: Int) {
                audioSessionIdLiveData.value = audioSessionId
            }
        })

        audioSessionIdLiveData.value = exoPlayer.audioSessionId
        playerNotificationManager.setPlayer(exoPlayer)

        exoPlayer.playWhenReady = true
        exoPlayer.prepare()

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
    }

    private fun initRadioPlayer(content: ServiceContentWrapper) {
        val mediaDataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(this)

        val mediaSource = ProgressiveMediaSource.Factory(mediaDataSourceFactory).createMediaSource(MediaItem.fromUri(
            Uri.parse(content.radioStation?.url)))

        val mediaSourceFactory: MediaSource.Factory = DefaultMediaSourceFactory(mediaDataSourceFactory)

        exoPlayer = ExoPlayer.Builder(this)
            .setMediaSourceFactory(mediaSourceFactory)
            .build()

        exoPlayer.addMediaSource(mediaSource)
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
        isRunningService = false
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        isRunningService = false
        //playerNotificationManager.setPlayer(null)
        stopSelf()
    }
}
