package com.hellguy39.collapse.presentaton.activities.main

import android.animation.LayoutTransition
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.exoplayer2.MediaMetadata
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.ActivityMainBinding
import com.hellguy39.collapse.presentaton.activities.track.TrackActivity
import com.hellguy39.collapse.presentaton.services.PlayerService
import com.hellguy39.collapse.presentaton.view_models.MediaLibraryDataViewModel
import com.hellguy39.collapse.presentaton.view_models.RadioStationsDataViewModel
import com.hellguy39.domain.models.*
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
import kotlinx.coroutines.withContext
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

        setObservers()

        binding.layoutCardPlayer.visibility = View.GONE
        binding.layoutCardPlayer.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

        //checkServiceSavedState()
    }

    override fun onRestart() {
        super.onRestart()
        //initSetupMediaLibraryDataViewModel()
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


//    private fun checkServiceSavedState() = CoroutineScope(Dispatchers.IO).launch {
//        val state = savedServiceStateUseCases.getSavedServiceStateUseCase.invoke()
//
//        when (state.playerType) {
//            PlayerType.LocalTrack -> {
//                val playlist: Playlist
//                val artist: Artist
//
//                if (state.playlistId != null) {
//                    when (state.playlistType) {
//                        PlaylistType.Artist -> {}
//                        PlaylistType.Favourites -> {}
//                        PlaylistType.AllTracks -> {}
//                        PlaylistType.Custom -> {}
//                    }
//
//                    PlayerService.startService(
//                        context = this@MainActivity,
//                        contentWrapper = ServiceContentWrapper(
//                            position = state.position,
//                            playerPosition = state.playerPosition,
//                            playlist = playlist,
//                            artist = artist,
//                            type = PlayerType.LocalTrack,
//                        )
//                    )
//                }
//
//            }
//            PlayerType.Radio -> {
//                val id = state.radioStationId
//                var radioStation = RadioStation()
//                if (id != null)
//                    radioStation = radioStationUseCases.getRadioStationByIdUseCase.invoke(id)
//                else
//                    return@launch
//
//                PlayerService.startService(
//                    context = this@MainActivity,
//                    contentWrapper = ServiceContentWrapper(
//                        type = PlayerType.Radio,
//                        radioStation = radioStation
//                    )
//                )
//            }
//        }
//    }

//        withContext(Dispatchers.Main) {
//            if (state.radioStation != null || state.playlist != null) {
//
//                if(state.radioStation != null)
//                    return@withContext //this is a temporary solution
//
//                PlayerService.startService(
//                    context = this@MainActivity,
//                    contentWrapper = state
//                )
//            }
//        }

    private fun updateCardUIWithMetadata(metadata: MediaMetadata) {

        if (PlayerService.isPlaying().value == true)
            binding.ibPlayPause.setImageResource(R.drawable.ic_round_pause_24)
        else
            binding.ibPlayPause.setImageResource(R.drawable.ic_round_play_arrow_24)

        binding.tvTrackName.text = if (metadata.title.isNullOrEmpty()) "Unknown" else metadata.title
        binding.tvArtist.text  = if (metadata.artist.isNullOrEmpty()) "Unknown" else metadata.artist

        val bytes = metadata.artworkData

        if (bytes != null)
            binding.ivTrackImage.setImageBitmap(convertByteArrayToBitmapUseCase.invoke(bytes))
        else
            binding.ivTrackImage.setImageResource(R.drawable.ic_round_audiotrack_24)

    }

    private fun updateCardUIWithRadioStation(radioStation: RadioStation) {

        if (PlayerService.isPlaying().value == true)
            binding.ibPlayPause.setImageResource(R.drawable.ic_round_pause_24)
        else
            binding.ibPlayPause.setImageResource(R.drawable.ic_round_play_arrow_24)

        binding.tvTrackName.text = radioStation.name
        binding.tvArtist.text = ""

        val bytes = radioStation.picture

        if (bytes != null)
            binding.ivTrackImage.setImageBitmap(convertByteArrayToBitmapUseCase.invoke(bytes))
        else
            binding.ivTrackImage.setImageResource(R.drawable.ic_round_radio_24)

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
                startActivity(Intent(this, TrackActivity::class.java))
            }
            binding.ibPlayPause.id -> {
                if (PlayerService.isPlaying().value == true) {
                    PlayerService.onPause()
                } else {
                    PlayerService.onPlay()
                }
            }
        }
    }
}