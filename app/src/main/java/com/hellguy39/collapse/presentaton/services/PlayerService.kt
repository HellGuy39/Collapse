package com.hellguy39.collapse.presentaton.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.audiofx.BassBoost
import android.media.audiofx.Equalizer
import android.media.audiofx.Virtualizer
import android.net.Uri
import android.os.IBinder
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.rtsp.RtspMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.hellguy39.collapse.presentaton.activities.main.DescriptionAdapter
import com.hellguy39.domain.models.ServiceContentWrapper
import com.hellguy39.domain.usecases.eq_settings.EqualizerSettingsUseCases
import com.hellguy39.domain.utils.PlayerType
import com.hellguy39.domain.utils.Protocol
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class PlayerService : Service() {

    @Inject
    lateinit var equalizerSettingsUseCases: EqualizerSettingsUseCases

    private lateinit var playerNotificationManager: PlayerNotificationManager

    private val notificationId = 101
    private val channelId = "player_channel"
    private val channelName = "foreground_player_channel"

    private val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()

    companion object {

        private lateinit var equalizer: Equalizer
        private lateinit var virtualizer: Virtualizer
        private lateinit var bassBoost: BassBoost

        private var numberOfPresets: Short = 0
        private var presetNames = mutableListOf<String>()

        private var lowestBandLevel: Short = -1500
        private var upperBandLevel: Short = 1500
        private var numberOfBands: Short = 5
        private val bandsCenterFreq = ArrayList<Int>(0)

        private var isRunningService = false
        private var playlistName: String = ""

        private lateinit var exoPlayer: ExoPlayer

        /*private var positionLiveData = MutableLiveData<Long>()*/
        private val mediaMetadataLiveData = MutableLiveData<MediaMetadata>()
        private val isPlayingLiveData = MutableLiveData<Boolean>()
        private val isEqualizerInitializedLiveData = MutableLiveData<Boolean>(false)
        private val audioSessionIdLiveData = MutableLiveData<Int>()
        private val playerTypeLiveData = MutableLiveData<Enum<PlayerType>>()
        private val contentPositionLiveData = MutableLiveData<Int>()
        private val isShuffleLiveData = MutableLiveData<Boolean>()

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

        fun isShuffle(): LiveData<Boolean> = isShuffleLiveData

        fun isPlaying(): LiveData<Boolean> = isPlayingLiveData
        fun getCurrentPosition(): Long = exoPlayer.currentPosition
        fun getCurrentMetadata(): LiveData<MediaMetadata> = mediaMetadataLiveData
        fun getContentPosition(): LiveData<Int> = contentPositionLiveData
        fun getDuration(): Long = exoPlayer.duration
        //fun getAudioSessionId(): LiveData<Int> = audioSessionIdLiveData
        fun isEqualizerInitialized(): LiveData<Boolean> = isEqualizerInitializedLiveData
        fun isRepeat(): Int = exoPlayer.repeatMode
        fun getPlaylistName(): String = playlistName

        //Control
        fun onPlay() = exoPlayer.play()
        fun onPause() = exoPlayer.pause()
        fun onNext() { if (exoPlayer.hasNextMediaItem()) exoPlayer.seekToNextMediaItem() }
        fun onPrevious() { if (exoPlayer.hasPreviousMediaItem()) exoPlayer.seekToPreviousMediaItem() }
        fun onShuffle(b: Boolean) {
            isShuffleLiveData.value = b
            exoPlayer.shuffleModeEnabled = b
        }
        fun onRepeat(n: Int) { exoPlayer.repeatMode = n }
        fun onSeekTo(pos: Long) = exoPlayer.seekTo(pos)

        //Equalizer
        fun enableEq(b: Boolean) = equalizer.setEnabled(b)

        fun setBandLevel(band: Short, value: Short) = equalizer.setBandLevel(band, value)
        fun setBassBoostBandLevel(value: Short) = bassBoost.setStrength(value)
        fun setVirtualizerBandLevel(value: Short) = virtualizer.setStrength(value)

        fun getLowestBandLevel(): Short = lowestBandLevel
        fun getUpperBandLevel(): Short = upperBandLevel
        fun getNumberOfBands(): Short = numberOfBands
        fun getBandsCenterFreq(): ArrayList<Int> = bandsCenterFreq

        fun getNumberOfPresets(): Short = numberOfPresets
        fun getPresetNames(): List<String> = presetNames
        fun usePreset(preset: Short) = equalizer.usePreset(preset)

        fun getBandsLevels(): List<Short> {
            val returnableList = mutableListOf<Short>()
            (0 until numberOfBands).forEach {
                returnableList.add(equalizer.getBandLevel(it.toShort()))
            }
            return returnableList
        }

        fun isBassBoostSupported(): Boolean = bassBoost.strengthSupported
        fun isVirtualizerSupported(): Boolean = virtualizer.strengthSupported
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
        playlistName = content.playlist?.name ?: ""

        exoPlayer = ExoPlayer.Builder(this).build()

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

        playerNotificationManager.setPlayer(exoPlayer)

        exoPlayer.playWhenReady = true
        exoPlayer.prepare()

        initEQ()
        setupEqSettings()

        return START_STICKY
    }

    private fun setupEqSettings() {

        val settings = equalizerSettingsUseCases.getEqualizerSettings.invoke()

        equalizer.enabled = settings.isEnabled

        if(settings.preset == -1) {
            equalizer.setBandLevel(0, (settings.band1Level).toInt().toShort())
            equalizer.setBandLevel(1, (settings.band2Level).toInt().toShort())
            equalizer.setBandLevel(2, (settings.band3Level).toInt().toShort())
            equalizer.setBandLevel(3, (settings.band4Level).toInt().toShort())
            equalizer.setBandLevel(4, (settings.band5Level).toInt().toShort())
        } else {
            equalizer.usePreset(settings.preset.toShort())
        }

        if(virtualizer.strengthSupported)
            virtualizer.setStrength((settings.bandVirtualizer).toInt().toShort())

        if(bassBoost.strengthSupported)
            bassBoost.setStrength((settings.bandVirtualizer).toInt().toShort())

    }

    private fun initEQ() {

        equalizer = Equalizer(1, exoPlayer.audioSessionId)
        virtualizer = Virtualizer(1, exoPlayer.audioSessionId)
        bassBoost = BassBoost(1, exoPlayer.audioSessionId)

        if (bassBoost.strengthSupported)
            bassBoost.enabled = true

        if (virtualizer.strengthSupported)
            virtualizer.enabled = true

        numberOfBands = equalizer.numberOfBands
        lowestBandLevel = equalizer.bandLevelRange[0]
        upperBandLevel = equalizer.bandLevelRange[1]

        (0 until numberOfBands)
            .map { equalizer.getCenterFreq(it.toShort()) }
            .mapTo(bandsCenterFreq) { it / 1000 }

        numberOfPresets = equalizer.numberOfPresets

        presetNames.clear()

        (0 until numberOfPresets).forEach { n ->
            presetNames.add(equalizer.getPresetName(n.toShort()))
        }

        presetNames.add("Custom")

        isEqualizerInitializedLiveData.value = true
    }

    private fun initDefaultPlayer(content: ServiceContentWrapper) {
        val trackList = content.playlist?.tracks

        if (trackList.isNullOrEmpty())
            return

        for (n in trackList.indices) {
            exoPlayer.addMediaItem(MediaItem.fromUri(trackList[n].path))
        }

        exoPlayer.seekToDefaultPosition(content.position)
    }

    private fun initRadioPlayer(content: ServiceContentWrapper) {

        val mediaItem = MediaItem.fromUri(Uri.parse(content.radioStation?.url))

        when(content.radioStation?.protocol) {
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
