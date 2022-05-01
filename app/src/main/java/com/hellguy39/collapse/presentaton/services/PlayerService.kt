package com.hellguy39.collapse.presentaton.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.CountDownTimer
import android.os.IBinder
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.rtsp.RtspMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.hellguy39.collapse.presentaton.adapters.DescriptionAdapter
import com.hellguy39.collapse.utils.AudioEffectController
import com.hellguy39.collapse.utils.StatisticController
import com.hellguy39.domain.models.RadioStation
import com.hellguy39.domain.models.SavedState
import com.hellguy39.domain.models.ServiceContentWrapper
import com.hellguy39.domain.models.Track
import com.hellguy39.domain.usecases.state.SavedServiceStateUseCases
import com.hellguy39.domain.utils.PlayerType
import com.hellguy39.domain.utils.PlaylistType
import com.hellguy39.domain.utils.Protocol
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.ThreadLocalRandom
import javax.inject.Inject


@AndroidEntryPoint
class PlayerService : LifecycleService() {

    @Inject
    lateinit var savedServiceStateUseCases: SavedServiceStateUseCases

    @Inject
    lateinit var effectController: AudioEffectController

    @Inject
    lateinit var statisticController: StatisticController

    private lateinit var playerNotificationManager: PlayerNotificationManager

    private val notificationId = 101
    private val channelId = "player_channel"
    private val channelName = "foreground_player_channel"

    private val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()

    companion object {

        private var isRunningService = MutableLiveData(false)
        private lateinit var exoPlayer: ExoPlayer

        private val serviceContentLiveData = MutableLiveData<ServiceContentWrapper>()
        private val mediaMetadataLiveData = MutableLiveData<MediaMetadata>()
        private val isPlayingLiveData = MutableLiveData<Boolean>()
        private val contentPositionLiveData = MutableLiveData<Int>()
        private val isShuffleLiveData = MutableLiveData<Boolean>()
        private val repeatModeLiveData = MutableLiveData(Player.REPEAT_MODE_OFF)
        private val deviceVolumeLiveData = MutableLiveData<Int>()
        private val trackDurationLiveData = MutableLiveData<Long>(0)

        fun startService(context: Context, contentWrapper: ServiceContentWrapper) {

            if(!contentWrapper.fromSavedState &&
                !contentWrapper.skipPauseClick &&
                contentWrapper.type == PlayerType.LocalTrack &&
                serviceContentLiveData.value?.playlist == contentWrapper.playlist &&
                    getContentPosition().value == contentWrapper.position) {

                if (isPlaying().value == true)
                    onPause()
                else
                    onPlay()

                return
            }

            updateContent(contentWrapper)

            if (isRunningService.value != true) {
                val service = Intent(context, PlayerService::class.java)
                ContextCompat.startForegroundService(context, service)
            }
        }

        private fun updateContent(content: ServiceContentWrapper) {
            serviceContentLiveData.value = content
        }

        fun getServiceContent(): ServiceContentWrapper {
            return ServiceContentWrapper(
                type = serviceContentLiveData.value?.type ?: PlayerType.Undefined,
                radioStation = serviceContentLiveData.value?.radioStation,
                position = exoPlayer.currentMediaItemIndex,
                playlist = serviceContentLiveData.value?.playlist,
                playerPosition = exoPlayer.contentPosition
            )
        }

        fun getCurrentTrack(): Track? {
            val pos = serviceContentLiveData.value?.position
            return if (pos != null)
                serviceContentLiveData.value?.playlist?.tracks?.get(pos)
            else null
        }

        fun getDeviceVolume(): LiveData<Int> = deviceVolumeLiveData
        fun setDeviceVolume(volume: Int) { exoPlayer.deviceVolume = volume }

        fun getDeviceInfo(): DeviceInfo = exoPlayer.deviceInfo

        fun isRunningService(): LiveData<Boolean> = isRunningService

        fun isShuffle(): LiveData<Boolean> = isShuffleLiveData
        fun isPlaying(): LiveData<Boolean> = isPlayingLiveData
        //fun getCurrentPosition(): Long = exoPlayer.currentPosition
        fun getCurrentMetadata(): LiveData<MediaMetadata> = mediaMetadataLiveData
        fun getContentPosition(): LiveData<Int> = contentPositionLiveData

        fun getDuration(): Long = exoPlayer.duration

        fun isRepeat(): Int = exoPlayer.repeatMode
        fun getRepeatMode(): LiveData<Int> = repeatModeLiveData

        fun getCurrentDuration(): LiveData<Long> = trackDurationLiveData

        //Control
        fun onPlay() {
            if (serviceContentLiveData.value?.type == PlayerType.Radio)
                exoPlayer.playWhenReady = true
            else
                exoPlayer.play()
        }
        fun onPause() {
            if (serviceContentLiveData.value?.type == PlayerType.Radio)
                exoPlayer.playWhenReady = false
            else
                exoPlayer.pause()
        }

        fun onNext() { if (exoPlayer.hasNextMediaItem()) exoPlayer.seekToNextMediaItem() }
        fun onPrevious() { if (exoPlayer.hasPreviousMediaItem()) exoPlayer.seekToPreviousMediaItem() }
        fun onShuffle(b: Boolean) {
            isShuffleLiveData.value = b
            exoPlayer.shuffleModeEnabled = b
        }
        fun onRepeat(n: Int) { exoPlayer.repeatMode = n }
        fun onSeekTo(pos: Long) {
            exoPlayer.seekTo(pos)
            trackDurationLiveData.value = pos
        }

        private var timer: CountDownTimer? = null
        private const val TIMER_INTERVAL: Long = 250
    }

    override fun onCreate() {
        super.onCreate()
        exoPlayer = ExoPlayer.Builder(this).build()

        effectController.init(exoPlayer.audioSessionId)

        deviceVolumeLiveData.value = exoPlayer.deviceVolume

        exoPlayer.addListener(getPlayerListener())

        isPlayingLiveData.value = false
        createNotificationsChannel()
    }

    private fun updateContent(content: ServiceContentWrapper) {
        setupPlayerNotificationManager(content.type, content.radioStation)
        exoPlayer.clearMediaItems()
        when(content.type) {
            PlayerType.LocalTrack -> {
                val trackList = content.playlist?.tracks
                if (!trackList.isNullOrEmpty())
                    initDefaultPlayer(trackList)
            }
            PlayerType.Radio -> {
                val radioStation = content.radioStation
                if (radioStation != null)
                    initRadioPlayer(radioStation)
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        isRunningService.value = true

        serviceContentLiveData.observe(this) {

            updateContent(it)

            if (it.startWithShuffle) {
                onShuffle(true)
                val maxShufflePos = it.playlist?.tracks?.size ?: 0
                val shufflePos = ThreadLocalRandom.current().nextInt(maxShufflePos)
                exoPlayer.seekToDefaultPosition(shufflePos)
            } else {
                exoPlayer.seekToDefaultPosition(it.position)
            }

            exoPlayer.prepare()

            if (it.fromSavedState) {
                exoPlayer.seekTo(it.playerPosition)
                exoPlayer.playWhenReady = false
            } else  {
                exoPlayer.playWhenReady = true
            }

            isRunningService.value = true
        }

        return START_STICKY
    }

    private fun setupPlayerNotificationManager(type: Enum<PlayerType>, radioStation: RadioStation?) {
        playerNotificationManager = PlayerNotificationManager
            .Builder(this, notificationId, channelId)
            .setMediaDescriptionAdapter(DescriptionAdapter(context = this, type = type, radioStation = radioStation))
            .setNotificationListener(getNotificationListener()).build()
        playerNotificationManager.setPlayer(exoPlayer)
    }

    private fun initDefaultPlayer(trackList: List<Track>) {

        if (trackList.isNullOrEmpty())
            return

        for (n in trackList.indices) {
            exoPlayer.addMediaItem(MediaItem.fromUri(trackList[n].path))
        }

    }

    private fun initRadioPlayer(radioStation: RadioStation) {

        val mediaItem = MediaItem.fromUri(Uri.parse(radioStation.url))

        when(radioStation.protocol) {
            Protocol.HLS -> {
                val mediaSource = HlsMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(mediaItem)
                exoPlayer.addMediaSource(mediaSource)
            }
            Protocol.DASH -> {
                val mediaSource: MediaSource = DashMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(mediaItem)
                exoPlayer.addMediaSource(mediaSource)
            }
            Protocol.Progressive -> {
                val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(mediaItem)
                exoPlayer.addMediaSource(mediaSource)
            }
            Protocol.RTSP -> {
                val mediaSource: MediaSource = RtspMediaSource.Factory()
                    .createMediaSource(mediaItem)
                exoPlayer.addMediaSource(mediaSource)
            }
            Protocol.SmoothStreaming -> {
                val mediaSource: MediaSource = SsMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(mediaItem)
                exoPlayer.addMediaSource(mediaSource)
            }
        }

    }

    private fun createNotificationsChannel() {
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_LOW,
        )
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(channel)
    }

    private fun startTimer(duration: Long) {

        val startFrom = trackDurationLiveData.value ?: 0
        var timerDuration = duration

        if (startFrom != 0.toLong()) {
            timerDuration = duration - startFrom
        }

        timer = object : CountDownTimer(timerDuration, TIMER_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                trackDurationLiveData.value = trackDurationLiveData.value?.plus(TIMER_INTERVAL)
                statisticController.updateTotalListeningTime(TIMER_INTERVAL)
            }

            override fun onFinish() {
                trackDurationLiveData.value = trackDurationLiveData.value?.plus(TIMER_INTERVAL)
                statisticController.updateTotalListeningTime(TIMER_INTERVAL)
            }
        }.start()
    }

    private fun stopTimer() {
        timer?.cancel()
    }

    private fun saveState() = CoroutineScope(Dispatchers.Main).launch {
        val content = getServiceContent()

        savedServiceStateUseCases.insertSavedServiceStateUseCase.invoke(
            savedState = SavedState(
                playerPosition = content.playerPosition,
                position = content.position,
                artistName = content.playlist?.name,
                playlistType = content.playlist?.type ?: PlaylistType.Undefined,
                playlistId = content.playlist?.id,
                radioStationId = content.radioStation?.id,
                playerType = content.type,
            )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.release()
        isRunningService.value = false
        saveState()
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        isRunningService.value = false
        saveState()
        stopSelf()
    }

    private fun getNotificationListener(): PlayerNotificationManager.NotificationListener =
        object : PlayerNotificationManager.NotificationListener {

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
    }

    private fun getPlayerListener() : Player.Listener = object : Player.Listener {

        override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
            mediaMetadataLiveData.value = mediaMetadata
            contentPositionLiveData.value = exoPlayer.currentMediaItemIndex
            trackDurationLiveData.value = 0
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            isPlayingLiveData.value = isPlaying

            if (isPlaying) {
                startTimer(exoPlayer.duration)
            } else {
                stopTimer()
            }
        }

        override fun onAudioSessionIdChanged(audioSessionId: Int) {
            effectController.init(audioSessionId)
        }

        override fun onPlayerError(error: PlaybackException) {
            super.onPlayerError(error)
            Toast.makeText(this@PlayerService, "Error: ${error.errorCodeName}", Toast.LENGTH_SHORT).show()
            stopSelf()
        }

        override fun onRepeatModeChanged(repeatMode: Int) {
            super.onRepeatModeChanged(repeatMode)
            repeatModeLiveData.value = repeatMode
        }

        override fun onDeviceVolumeChanged(volume: Int, muted: Boolean) {
            super.onDeviceVolumeChanged(volume, muted)
            deviceVolumeLiveData.value = volume
        }
    }
}
