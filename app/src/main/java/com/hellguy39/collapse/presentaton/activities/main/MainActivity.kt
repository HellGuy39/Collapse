package com.hellguy39.collapse.presentaton.activities.main

import android.animation.LayoutTransition
import android.app.ActivityOptions
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.TypedValue
import android.view.View
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.palette.graphics.Palette
import com.google.android.exoplayer2.MediaMetadata
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.ActivityMainBinding
import com.hellguy39.collapse.presentaton.activities.track.TrackActivity
import com.hellguy39.collapse.presentaton.services.PlayerService
import com.hellguy39.collapse.presentaton.view_models.MediaLibraryDataViewModel
import com.hellguy39.collapse.presentaton.view_models.RadioStationsDataViewModel
import com.hellguy39.domain.models.Playlist
import com.hellguy39.domain.models.RadioStation
import com.hellguy39.domain.models.ServiceContentWrapper
import com.hellguy39.domain.usecases.ConvertByteArrayToBitmapUseCase
import com.hellguy39.domain.usecases.favourites.FavouriteTracksUseCases
import com.hellguy39.domain.usecases.playlist.PlaylistUseCases
import com.hellguy39.domain.usecases.radio.RadioStationUseCases
import com.hellguy39.domain.usecases.state.SavedServiceStateUseCases
import com.hellguy39.domain.usecases.tracks.TracksUseCases
import com.hellguy39.domain.utils.PlayerType
import com.hellguy39.domain.utils.PlaylistType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), View.OnClickListener {

    @Inject
    lateinit var radioStationUseCases: RadioStationUseCases

    @Inject
    lateinit var savedServiceStateUseCases: SavedServiceStateUseCases

    @Inject
    lateinit var convertByteArrayToBitmapUseCase: ConvertByteArrayToBitmapUseCase

    @Inject
    lateinit var playlistUseCases: PlaylistUseCases

    @Inject
    lateinit var tracksUseCases: TracksUseCases

    @Inject
    lateinit var favouriteTracksUseCases: FavouriteTracksUseCases

    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var binding: ActivityMainBinding

    private lateinit var mediaLibraryDataViewModel: MediaLibraryDataViewModel
    private lateinit var radioStationsDataViewModel: RadioStationsDataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mediaLibraryDataViewModel = ViewModelProvider(this)[MediaLibraryDataViewModel::class.java]
        radioStationsDataViewModel = ViewModelProvider(this)[RadioStationsDataViewModel::class.java]

        navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHostFragment.navController

        binding.rootLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

        NavigationUI.setupWithNavController(
            binding.bottomNavigation,navController
        )

        binding.trackCard.setOnClickListener(this)
        binding.ibPlayPause.setOnClickListener(this)
        binding.ibNextTrack.setOnClickListener(this)

        setObservers()

        binding.layoutCardPlayer.visibility = View.GONE
        binding.layoutCardPlayer.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

        checkServiceSavedState()
    }

    override fun onRestart() {
        super.onRestart()
        initSetupMediaLibraryDataViewModel()
    }

    private fun setObservers() {
        PlayerService.isPlaying().observe(this) {
            if (it) {
                binding.ibPlayPause.setImageResource(R.drawable.ic_round_pause_24)
            } else {
                binding.ibPlayPause.setImageResource(R.drawable.ic_round_play_arrow_24)
            }
        }

        PlayerService.isRunningService().observe(this) { isRunning ->
            if (isRunning) {
                when(PlayerService.getServiceContent().type) {
                    PlayerType.LocalTrack -> {
                        val metadata = PlayerService.getCurrentMetadata().value
                        if (metadata != null)
                            updateCardUIWithMetadata(metadata)
                    }
                    PlayerType.Radio -> {
                        val radioStation = PlayerService.getServiceContent().radioStation

                        if (radioStation != null)
                            updateCardUIWithRadioStation(radioStation)
                    }
                }
                showTrackCard()
            } else {
                hideTrackCard()
            }
        }

        PlayerService.getCurrentMetadata().observe(this) { metadata ->
            val type = PlayerService.getServiceContent().type

            when (type) {
                PlayerType.Radio -> {

                }
                PlayerType.LocalTrack -> {
                    updateCardUIWithMetadata(metadata)
                }
            }
        }
    }

    fun initSetupMediaLibraryDataViewModel() {
        mediaLibraryDataViewModel.initSetup()
    }


    private fun checkServiceSavedState() = CoroutineScope(Dispatchers.IO).launch {
        val savedState = savedServiceStateUseCases.getSavedServiceStateUseCase.invoke()

        when(savedState.playerType) {
            PlayerType.LocalTrack -> {
                val playlistId = savedState.playlistId
                val artistName = savedState.artistName
                val position = savedState.position
                val playerPosition = savedState.playerPosition

                when(savedState.playlistType) {
                    PlaylistType.AllTracks -> {
                        val trackList = tracksUseCases.getAllTracksUseCase.invoke()
                        startPlayer(receivedPlaylist = Playlist(
                            tracks = trackList.toMutableList(),
                            name = "All tracks",
                            type = PlaylistType.AllTracks
                        ),
                            position = position,
                            playerPosition = playerPosition,
                            skipPauseClick = true,
                            startWithShuffle = false
                        )
                    }
                    PlaylistType.Favourites -> {
                        val favourites = favouriteTracksUseCases.getAllFavouriteTracksUseCase.invoke()
                        startPlayer(receivedPlaylist = Playlist(
                            tracks = favourites.toMutableList(),
                            name = "All tracks",
                            type = PlaylistType.AllTracks
                        ),
                            position = position,
                            playerPosition = playerPosition,
                            skipPauseClick = true,
                            startWithShuffle = false
                        )
                    }
                    PlaylistType.Artist -> {
                        if (artistName != null) {
                            val artistTrackList = tracksUseCases.getAllTrackByArtistUseCase.invoke(artistName)
                            startPlayer(receivedPlaylist = Playlist(
                                tracks = artistTrackList.toMutableList(),
                                name = artistName,
                                type = PlaylistType.Artist
                                ),
                                position = position,
                                playerPosition = playerPosition,
                                skipPauseClick = true,
                                startWithShuffle = false
                            )
                        }
                    }
                    PlaylistType.Custom -> {
                        if (playlistId != null) {
                            val playlist = playlistUseCases.getPlaylistByIdUseCase.invoke(id = playlistId)
                            startPlayer(
                                receivedPlaylist = playlist,
                                position = position,
                                skipPauseClick = true,
                                startWithShuffle = false,
                                playerPosition = playerPosition
                            )
                        }
                    }
                    PlaylistType.Undefined -> {

                    }
                    else -> {}
                }

            }
            PlayerType.Radio -> {

            }
            PlayerType.Undefined -> {

            }
            else -> {}
        }
    }


    private fun startPlayer(
        receivedPlaylist: Playlist,
        position: Int,
        startWithShuffle: Boolean = false,
        skipPauseClick: Boolean = false,
        playerPosition: Long
    ) = CoroutineScope(Dispatchers.Main).launch {
        if (receivedPlaylist.tracks.size == 0) {
            return@launch
        }

        PlayerService.startService(this@MainActivity, ServiceContentWrapper(
            type = PlayerType.LocalTrack,
            position = position,
            playlist = receivedPlaylist,
            playerPosition = playerPosition,
            startWithShuffle = startWithShuffle,
            skipPauseClick = skipPauseClick,
            fromSavedState = true
            )
        )
    }

    private fun updateCardUIWithMetadata(metadata: MediaMetadata) {

        binding.ibNextTrack.visibility = View.VISIBLE

        if (PlayerService.isPlaying().value == true)
            binding.ibPlayPause.setImageResource(R.drawable.ic_round_pause_24)
        else
            binding.ibPlayPause.setImageResource(R.drawable.ic_round_play_arrow_24)

        binding.tvTrackName.text = if (metadata.title.isNullOrEmpty()) "Unknown" else metadata.title
        binding.tvArtist.text  = if (metadata.artist.isNullOrEmpty()) "Unknown" else metadata.artist

        val bytes = metadata.artworkData

        if (bytes != null) {
            val white = ResourcesCompat.getColor(resources, R.color.white,null)
            val bitmap = convertByteArrayToBitmapUseCase.invoke(bytes)
            binding.ivTrackImage.setImageBitmap(bitmap)
            updatePalette(bitmap)
            binding.tvTrackName.setTextColor(white)
            binding.tvArtist.setTextColor(white)
            binding.ibPlayPause.imageTintList = ColorStateList.valueOf(white)
            binding.ibNextTrack.imageTintList = ColorStateList.valueOf(white)
        } else {
            val typedValue = TypedValue()
            val theme = this.theme
            theme.resolveAttribute(com.google.android.material.R.attr.colorOnSurface, typedValue, true)
            @ColorInt val colorOnSurface = typedValue.data

            binding.ivTrackImage.setImageResource(R.drawable.ic_round_audiotrack_24)
            binding.trackCard.backgroundTintList = null
            binding.ibPlayPause.imageTintList = ColorStateList.valueOf(colorOnSurface)
            binding.ibNextTrack.imageTintList = ColorStateList.valueOf(colorOnSurface)
            binding.tvTrackName.setTextColor(colorOnSurface)
            binding.tvArtist.setTextColor(colorOnSurface)
        }

    }

    private fun updateCardUIWithRadioStation(radioStation: RadioStation) {

        binding.ibNextTrack.visibility = View.GONE

        if (PlayerService.isPlaying().value == true)
            binding.ibPlayPause.setImageResource(R.drawable.ic_round_pause_24)
        else
            binding.ibPlayPause.setImageResource(R.drawable.ic_round_play_arrow_24)

        binding.tvTrackName.text = radioStation.name
        binding.tvArtist.text = ""

        val bytes = radioStation.picture

        if (bytes != null) {
            val white = ResourcesCompat.getColor(resources, R.color.white,null)
            val bitmap = convertByteArrayToBitmapUseCase.invoke(bytes)
            binding.ivTrackImage.setImageBitmap(bitmap)
            updatePalette(bitmap)
            binding.tvTrackName.setTextColor(white)
            binding.tvArtist.setTextColor(white)
            binding.ibPlayPause.imageTintList = ColorStateList.valueOf(white)
            binding.ibNextTrack.imageTintList = ColorStateList.valueOf(white)
        } else {
            val typedValue = TypedValue()
            val theme = this.theme
            theme.resolveAttribute(com.google.android.material.R.attr.colorOnSurface, typedValue, true)
            @ColorInt val colorOnSurface = typedValue.data

            binding.ivTrackImage.setImageResource(R.drawable.ic_round_radio_24)
            binding.trackCard.backgroundTintList = null
            binding.trackCard.backgroundTintList = null
            binding.ibPlayPause.imageTintList = ColorStateList.valueOf(colorOnSurface)
            binding.ibNextTrack.imageTintList = ColorStateList.valueOf(colorOnSurface)
            binding.tvTrackName.setTextColor(colorOnSurface)
            binding.tvArtist.setTextColor(colorOnSurface)
        }

    }

    private fun updatePalette(bitmap: Bitmap) {
        Palette.from(bitmap).generate { palette ->
            if (palette != null) {
                binding.trackCard.backgroundTintList = ColorStateList.valueOf(palette.getMutedColor(1))
            }
        }
    }


    private fun showTrackCard() {

        binding.ibPlayPause.setImageResource(R.drawable.ic_round_pause_24)

        TransitionManager.beginDelayedTransition(binding.layoutCardPlayer, AutoTransition())
        binding.layoutCardPlayer.visibility = View.VISIBLE
    }

    private fun hideTrackCard() {
        TransitionManager.beginDelayedTransition(binding.layoutCardPlayer, AutoTransition())
        binding.layoutCardPlayer.visibility = View.GONE
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            binding.trackCard.id -> {
                val options = ActivityOptions.makeSceneTransitionAnimation(this)
                startActivity(Intent(this, TrackActivity::class.java), options.toBundle())
            }
            binding.ibPlayPause.id -> {
                if (PlayerService.isPlaying().value == true) {
                    PlayerService.onPause()
                } else {
                    PlayerService.onPlay()
                }
            }
            binding.ibNextTrack.id -> {
                PlayerService.onNext()
            }
        }
    }
}