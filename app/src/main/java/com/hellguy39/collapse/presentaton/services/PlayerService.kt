package com.hellguy39.collapse.presentaton.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
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
import com.hellguy39.collapse.controllers.StatisticController
import com.hellguy39.collapse.controllers.audio_effect.AudioEffectController
import com.hellguy39.collapse.presentaton.adapters.DescriptionAdapter
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

    private val metadataBuilder = MediaMetadataCompat.Builder()

    private val stateBuilder = PlaybackStateCompat.Builder().setActions(
        PlaybackStateCompat.ACTION_PLAY
                or PlaybackStateCompat.ACTION_STOP
                or PlaybackStateCompat.ACTION_PAUSE
                or PlaybackStateCompat.ACTION_PLAY_PAUSE
                or PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
    )

    private var mediaSession: MediaSessionCompat? = null

    private val audioManager: AudioManager? = null
    private var audioFocusRequest: AudioFocusRequest? = null
    private var audioFocusRequested = false

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
            val pos = contentPositionLiveData.value
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

//        fun setAuxEffect(id: Int) {
//            exoPlayer.setAuxEffectInfo(AuxEffectInfo(id, 1f))
//        }

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

        val audioAttributes: AudioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()
//        audioFocusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
//            .setOnAudioFocusChangeListener(audioFocusChangeListener)
//            .setAcceptsDelayedFocusGain(false)
//            .setWillPauseWhenDucked(true)
//            .setAudioAttributes(audioAttributes)
//            .build()

        mediaSession = MediaSessionCompat(this, "PlayerService")
        //mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)

        //mediaSession?.setCallback(mediaSessionCallback)

        exoPlayer = ExoPlayer.Builder(this).build()

        effectController.updateAudioSession(exoPlayer.audioSessionId)

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

        if (trackList.isEmpty())
            return

        for (track in trackList)
            exoPlayer.addMediaItem(MediaItem.fromUri(track.path))
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

            //Log.d("DEBUG", exoPlayer.media)
            Log.d("DEBUG", "IS PLAYABLE: ${mediaMetadata}\tIndex: ${exoPlayer.currentMediaItemIndex}")
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
            effectController.updateAudioSession(audioSessionId)
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

//    private val mediaSessionCallback: MediaSessionCompat.Callback =
//        object : MediaSessionCompat.Callback() {
//            private var currentUri: Uri? = null
//            var currentState = PlaybackStateCompat.STATE_STOPPED
//            override fun onPlay() {
//                if (!exoPlayer.playWhenReady) {
//                    startService(Intent(applicationContext, PlayerService::class.java))
//                    val track: MusicRepository.Track = musicRepository.getCurrent()
//                    updateMetadataFromTrack(track)
//                    prepareToPlay(track.getUri())
//                    if (!audioFocusRequested) {
//                        audioFocusRequested = true
//                        val audioFocusResult: Int
//                        audioFocusResult = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                            audioManager!!.requestAudioFocus(audioFocusRequest!!)
//                        } else {
//                            audioManager!!.requestAudioFocus(
//                                audioFocusChangeListener,
//                                AudioManager.STREAM_MUSIC,
//                                AudioManager.AUDIOFOCUS_GAIN
//                            )
//                        }
//                        if (audioFocusResult != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) return
//                    }
//                    mediaSession!!.isActive = true // Сразу после получения фокуса
//                    registerReceiver(
//                        becomingNoisyReceiver,
//                        IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY)
//                    )
//                    exoPlayer.playWhenReady = true
//                }
//                mediaSession!!.setPlaybackState(
//                    stateBuilder.setState(
//                        PlaybackStateCompat.STATE_PLAYING,
//                        PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN,
//                        1f
//                    ).build()
//                )
//                currentState = PlaybackStateCompat.STATE_PLAYING
//                refreshNotificationAndForegroundStatus(currentState)
//            }
//
//            override fun onPause() {
//                if (exoPlayer.playWhenReady) {
//                    exoPlayer.playWhenReady = false
//                    unregisterReceiver(becomingNoisyReceiver)
//                }
//                mediaSession!!.setPlaybackState(
//                    stateBuilder.setState(
//                        PlaybackStateCompat.STATE_PAUSED,
//                        PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN,
//                        1f
//                    ).build()
//                )
//                currentState = PlaybackStateCompat.STATE_PAUSED
//                refreshNotificationAndForegroundStatus(currentState)
//            }
//
//            override fun onStop() {
//                if (exoPlayer.playWhenReady) {
//                    exoPlayer.playWhenReady = false
//                    unregisterReceiver(becomingNoisyReceiver)
//                }
//                if (audioFocusRequested) {
//                    audioFocusRequested = false
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                        audioManager!!.abandonAudioFocusRequest(audioFocusRequest!!)
//                    } else {
//                        audioManager!!.abandonAudioFocus(audioFocusChangeListener)
//                    }
//                }
//                mediaSession!!.isActive = false
//                mediaSession!!.setPlaybackState(
//                    stateBuilder.setState(
//                        PlaybackStateCompat.STATE_STOPPED,
//                        PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN,
//                        1f
//                    ).build()
//                )
//                currentState = PlaybackStateCompat.STATE_STOPPED
//                refreshNotificationAndForegroundStatus(currentState)
//                stopSelf()
//            }
//
//            override fun onSkipToNext() {
//                val track: MusicRepository.Track = musicRepository.getNext()
//                updateMetadataFromTrack(track)
//                refreshNotificationAndForegroundStatus(currentState)
//                prepareToPlay(track.getUri())
//            }
//
//            override fun onSkipToPrevious() {
//                val track: MusicRepository.Track = musicRepository.getPrevious()
//                updateMetadataFromTrack(track)
//                refreshNotificationAndForegroundStatus(currentState)
//                prepareToPlay(track.getUri())
//            }
//
//            private fun prepareToPlay(uri: Uri) {
//                if (uri != currentUri) {
//                    currentUri = uri
//                    val mediaSource =
//                        ExtractorMediaSource(uri, dataSourceFactory, extractorsFactory, null, null)
//                    exoPlayer.prepare(mediaSource)
//                }
//            }
//
//            private fun updateMetadataFromTrack(track: MusicRepository.Track) {
//                metadataBuilder.putBitmap(
//                    MediaMetadataCompat.METADATA_KEY_ART, BitmapFactory.decodeResource(
//                        resources, track.getBitmapResId()
//                    )
//                )
//                metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_TITLE, track.getTitle())
//                metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_ALBUM, track.getArtist())
//                metadataBuilder.putString(
//                    MediaMetadataCompat.METADATA_KEY_ARTIST,
//                    track.getArtist()
//                )
//                metadataBuilder.putLong(
//                    MediaMetadataCompat.METADATA_KEY_DURATION,
//                    track.getDuration()
//                )
//                mediaSession!!.setMetadata(metadataBuilder.build())
//            }
//        }

//    class PlayerServiceBinder : Binder() {
//        val mediaSessionToken: MediaSessionCompat.Token
//            get() = mediaSession.getSessionToken()
//    }
}
