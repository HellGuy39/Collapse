package com.hellguy39.collapse.presentaton.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.audiofx.BassBoost
import android.media.audiofx.Equalizer
import android.media.audiofx.Virtualizer
import android.net.Uri
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
import com.hellguy39.domain.models.*
import com.hellguy39.domain.usecases.eq_settings.EqualizerSettingsUseCases
import com.hellguy39.domain.usecases.favourites.FavouriteTracksUseCases
import com.hellguy39.domain.usecases.playlist.PlaylistUseCases
import com.hellguy39.domain.usecases.radio.RadioStationUseCases
import com.hellguy39.domain.usecases.state.SavedServiceStateUseCases
import com.hellguy39.domain.usecases.tracks.TracksUseCases
import com.hellguy39.domain.utils.PlayerType
import com.hellguy39.domain.utils.PlaylistType
import com.hellguy39.domain.utils.Protocol
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.util.concurrent.ThreadLocalRandom
import javax.inject.Inject


@AndroidEntryPoint
class PlayerService : LifecycleService() {

    @Inject
    lateinit var equalizerSettingsUseCases: EqualizerSettingsUseCases

    @Inject
    lateinit var savedServiceStateUseCases: SavedServiceStateUseCases

    @Inject
    lateinit var playlistUseCases: PlaylistUseCases

    @Inject
    lateinit var radioStationUseCases: RadioStationUseCases

    @Inject
    lateinit var favouriteTracksUseCases: FavouriteTracksUseCases

    @Inject
    lateinit var tracksUseCases: TracksUseCases

    private lateinit var playerNotificationManager: PlayerNotificationManager

    private val notificationId = 101
    private val channelId = "player_channel"
    private val channelName = "foreground_player_channel"

    private val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()

    companion object {

        private lateinit var virtualizer: Virtualizer
        private lateinit var bassBoost: BassBoost

        private var equalizerModel: EqualizerModel = EqualizerModel()

        private var isRunningService = MutableLiveData(false)
        private lateinit var exoPlayer: ExoPlayer

        private val serviceContentLiveData = MutableLiveData<ServiceContentWrapper>()
        private val mediaMetadataLiveData = MutableLiveData<MediaMetadata>()
        private val isPlayingLiveData = MutableLiveData<Boolean>()
        private val isEqualizerInitializedLiveData = MutableLiveData(false)
        private val audioSessionIdLiveData = MutableLiveData<Int>()
        private val contentPositionLiveData = MutableLiveData<Int>()
        private val isShuffleLiveData = MutableLiveData<Boolean>()
        private val repeatModeLiveData = MutableLiveData(Player.REPEAT_MODE_OFF)
        private val deviceVolumeLiveData = MutableLiveData<Int>()

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
        fun getCurrentPosition(): Long = exoPlayer.currentPosition
        fun getCurrentMetadata(): LiveData<MediaMetadata> = mediaMetadataLiveData
        fun getContentPosition(): LiveData<Int> = contentPositionLiveData
        fun getDuration(): Long = exoPlayer.duration
        fun isEqualizerInitialized(): LiveData<Boolean> = isEqualizerInitializedLiveData
        fun isRepeat(): Int = exoPlayer.repeatMode
        fun getRepeatMode(): LiveData<Int> = repeatModeLiveData

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
        fun onSeekTo(pos: Long) = exoPlayer.seekTo(pos)

        fun enableEq(b: Boolean) { equalizerModel.equalizer?.setEnabled(b) }
        fun enableBass(b: Boolean) = bassBoost.setEnabled(b)
        fun enableVirtualizer(b: Boolean) = virtualizer.setEnabled(b)

        fun setBandLevel(band: Short, value: Short) = equalizerModel.equalizer?.setBandLevel(band, value)
        fun setBassBoostBandLevel(value: Short) {
            bassBoost.setStrength(value)
        }

        fun setVirtualizerBandLevel(value: Short) = virtualizer.setStrength(value)

        fun getLowestBandLevel(): Short = equalizerModel.lowestBandLevel
        fun getUpperBandLevel(): Short = equalizerModel.upperBandLevel
        //fun getNumberOfBands(): Short = equalizerModel.numberOfBands
        fun getBandsCenterFreq(): ArrayList<Int> = equalizerModel.bandsCenterFreq

        //fun getNumberOfPresets(): Short = equalizerModel.numberOfPresets
        fun getPresetNames(): List<String> = equalizerModel.presetNames
        fun usePreset(preset: Short) = equalizerModel.equalizer?.usePreset(preset)

        fun getBandsLevels(): List<Short> {
            val returnableList = mutableListOf<Short>()
            if (equalizerModel.equalizer != null) {
                (0 until equalizerModel.numberOfBands).forEach {
                    returnableList.add(equalizerModel.equalizer!!.getBandLevel(it.toShort()))
                }
            }
            return returnableList
        }

        fun isBassBoostSupported(): Boolean = bassBoost.strengthSupported
        fun isVirtualizerSupported(): Boolean = virtualizer.strengthSupported
    }

    override fun onCreate() {
        super.onCreate()
        exoPlayer = ExoPlayer.Builder(this).build()

        deviceVolumeLiveData.value = exoPlayer.deviceVolume

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
        })

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

        initEQ()
        setupEqSettings()

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
        playerNotificationManager = PlayerNotificationManager.Builder(
            this,
            notificationId,
            channelId
        ).setMediaDescriptionAdapter(DescriptionAdapter(context = this, type = type, radioStation = radioStation)
        ).setNotificationListener(object : PlayerNotificationManager.NotificationListener {
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
            }).build()
        playerNotificationManager.setPlayer(exoPlayer)
    }

    private fun setupEqSettings() {

        val equalizer = equalizerModel.equalizer ?: return

        val settings = equalizerSettingsUseCases.getEqualizerSettings.invoke()

        equalizer.enabled = settings.isEqEnabled

        if(settings.preset == -1) {
            equalizer.setBandLevel(0, (settings.band1Level).toInt().toShort())
            equalizer.setBandLevel(1, (settings.band2Level).toInt().toShort())
            equalizer.setBandLevel(2, (settings.band3Level).toInt().toShort())
            equalizer.setBandLevel(3, (settings.band4Level).toInt().toShort())
            equalizer.setBandLevel(4, (settings.band5Level).toInt().toShort())
        } else {
            equalizer.usePreset(settings.preset.toShort())
        }

        if(virtualizer.strengthSupported) {
            virtualizer.enabled = settings.isVirtualizerEnabled
            virtualizer.setStrength((settings.bandVirtualizer).toInt().toShort())
        }

        if(bassBoost.strengthSupported) {
            bassBoost.enabled = settings.isBassEnabled
            bassBoost.setStrength((settings.bandVirtualizer).toInt().toShort())
        }
    }

    private fun initEQ() {

        equalizerModel.equalizer = Equalizer(1000, exoPlayer.audioSessionId)
        virtualizer = Virtualizer(1000, exoPlayer.audioSessionId)
        bassBoost = BassBoost(1000, exoPlayer.audioSessionId)

        equalizerModel.numberOfBands = equalizerModel.equalizer!!.numberOfBands
        equalizerModel.lowestBandLevel = equalizerModel.equalizer!!.bandLevelRange[0]
        equalizerModel.upperBandLevel = equalizerModel.equalizer!!.bandLevelRange[1]

        (0 until equalizerModel.numberOfBands)
            .map { equalizerModel.equalizer!!.getCenterFreq(it.toShort()) }
            .mapTo(equalizerModel.bandsCenterFreq) { it / 1000 }

        equalizerModel.numberOfPresets = equalizerModel.equalizer!!.numberOfPresets

        equalizerModel.presetNames.clear()

        (0 until equalizerModel.numberOfPresets).forEach { n ->
            equalizerModel.presetNames.add(equalizerModel.equalizer!!.getPresetName(n.toShort()))
        }

        equalizerModel.presetNames.add("Custom")

        isEqualizerInitializedLiveData.value = true
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
}
