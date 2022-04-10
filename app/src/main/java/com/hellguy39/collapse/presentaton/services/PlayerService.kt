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
import android.util.Log
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
import com.hellguy39.domain.models.RadioStation
import com.hellguy39.domain.models.ServiceContentWrapper
import com.hellguy39.domain.models.Track
import com.hellguy39.domain.usecases.eq_settings.EqualizerSettingsUseCases
import com.hellguy39.domain.usecases.favourites.FavouriteTracksUseCases
import com.hellguy39.domain.usecases.playlist.PlaylistUseCases
import com.hellguy39.domain.usecases.radio.RadioStationUseCases
import com.hellguy39.domain.usecases.state.SavedServiceStateUseCases
import com.hellguy39.domain.usecases.tracks.TracksUseCases
import com.hellguy39.domain.utils.PlayerType
import com.hellguy39.domain.utils.Protocol
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

        private lateinit var equalizer: Equalizer
        private lateinit var virtualizer: Virtualizer
        private lateinit var bassBoost: BassBoost
        private var numberOfPresets: Short = 0
        private var presetNames = mutableListOf<String>()
        private var lowestBandLevel: Short = -1500
        private var upperBandLevel: Short = 1500
        private var numberOfBands: Short = 5
        private val bandsCenterFreq = ArrayList<Int>(0)

        private var isRunningService = MutableLiveData<Boolean>(false)
        private lateinit var exoPlayer: ExoPlayer
        private val serviceContentLiveData = MutableLiveData<ServiceContentWrapper>()
        /*private var positionLiveData = MutableLiveData<Long>()*/
        private val mediaMetadataLiveData = MutableLiveData<MediaMetadata>()
        private val isPlayingLiveData = MutableLiveData<Boolean>()
        private val isEqualizerInitializedLiveData = MutableLiveData<Boolean>(false)
        private val audioSessionIdLiveData = MutableLiveData<Int>()
        //private val playerTypeLiveData = MutableLiveData<Enum<PlayerType>>()
        private val contentPositionLiveData = MutableLiveData<Int>()
        private val isShuffleLiveData = MutableLiveData<Boolean>()

        fun startService(context: Context, contentWrapper: ServiceContentWrapper) {

            updateContent(contentWrapper)

            if (isRunningService.value != true) {
                val service = Intent(context, PlayerService::class.java)
                //service.putExtra("track_list", contentWrapper)
                ContextCompat.startForegroundService(context, service)
            }
        }

        fun stopService(context: Context) {
            val service = Intent(context, PlayerService::class.java)
            context.stopService(service)
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

        fun isRunningService(): LiveData<Boolean> = isRunningService

        fun isShuffle(): LiveData<Boolean> = isShuffleLiveData
        fun isPlaying(): LiveData<Boolean> = isPlayingLiveData
        fun getCurrentPosition(): Long = exoPlayer.currentPosition
        fun getCurrentMetadata(): LiveData<MediaMetadata> = mediaMetadataLiveData
        fun getContentPosition(): LiveData<Int> = contentPositionLiveData
        fun getDuration(): Long = exoPlayer.duration
        fun isEqualizerInitialized(): LiveData<Boolean> = isEqualizerInitializedLiveData
        fun isRepeat(): Int = exoPlayer.repeatMode

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
        exoPlayer = ExoPlayer.Builder(this).build()

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
        })

        isPlayingLiveData.value = false
        createNotificationsChannel()
    }

    private fun updateContent(content: ServiceContentWrapper) {
        setupPlayerNotificationManager(content.type, content.radioStation)
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
            //setupExoPlayer(serviceContentWrapper)
            updateContent(it)
            exoPlayer.seekToDefaultPosition(it.position)
            exoPlayer.playWhenReady = true
            exoPlayer.prepare()
        }

//        CoroutineScope(Dispatchers.IO).launch {
//            //setupContent(content)
//            withContext(Dispatchers.Main) {
//                setupExoPlayer(serviceContentWrapper)
//                initEQ()
//                setupEqSettings()
//                isRunningService.value = true
//            }
//        }

        return START_STICKY
    }

//    private fun setupExoPlayer(content: ServiceContentWrapper) {
//
//        when(content.type) {
//            PlayerType.LocalTrack -> initDefaultPlayer(content.playlist?.tracks ?: listOf())
//            PlayerType.Radio -> initRadioPlayer(content.radioStation ?: RadioStation())
//        }
//        exoPlayer.seekToDefaultPosition(content.position)
//
////        if (content.fromSavedState) {
////            val pos = content.playerPosition
////            exoPlayer.seekTo(pos)
////            exoPlayer.playWhenReady = false
////        } else {
////            exoPlayer.playWhenReady = true
////        }
//
//        exoPlayer.prepare()
//    }

//    private suspend fun setupContent(content: ServiceContentWrapper) {
//
//        if (content.radioStation != null && content.radioStation?.id != -1)
//            radioStation = content.radioStation!!
//
//        if (content.fromSavedState) {
//            val radioStationId = content.radioStation?.id
//            val playlistId = content.playlist?.id
//
//            if (radioStationId != null && radioStationId != -1)
//                content.radioStation = radioStationUseCases.getRadioStationByIdUseCase.invoke(
//                    radioStationId
//                )
//
//
//            if (playlistId != null && playlistId != -1) {
//
//                if(content.playlist == null)
//                    return
//
//                val type = content.playlist!!.type
//
//                when(type) {
//                    PlaylistType.AllTracks -> {
//                        content.playlist = Playlist(
//                            name = "All tracks",
//                            type = PlaylistType.AllTracks,
//                            tracks = tracksUseCases.getAllTracksUseCase.invoke()
//                        )
//                    }
//                    PlaylistType.Favourites -> {
//                        content.playlist = Playlist(
//                            name = "Favourites",
//                            type = PlaylistType.Favourites,
//                            tracks = favouriteTracksUseCases.getAllFavouriteTracksUseCase.invoke()
//                        )
//                    }
//                    PlaylistType.Custom -> {
//                        content.playlist = playlistUseCases.getPlaylistByIdUseCase.invoke(
//                            playlistId
//                        )
//                    }
//                    PlaylistType.Artist -> {
//                        //stopSelf()
//                    }
//                    PlaylistType.Undefined -> {
//                        //stopSelf()
//                    }
//                }
//            }
//        }
//
//        serviceContentWrapper = content
//    }

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
            })
            .build()
        playerNotificationManager.setPlayer(exoPlayer)
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

    private fun initDefaultPlayer(trackList: List<Track>) {

        if (trackList.isNullOrEmpty())
            return

        exoPlayer.clearMediaItems()

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
            NotificationManager.IMPORTANCE_LOW
        )
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(channel)
    }

    private fun saveState() = CoroutineScope(Dispatchers.Main).launch {
        savedServiceStateUseCases.insertSavedServiceStateUseCase.invoke(
            serviceContentWrapper = getServiceContent()
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
        //playerNotificationManager.setPlayer(null)
        stopSelf()
    }
}
