package com.hellguy39.collapse.presentaton.activities.main

import android.animation.LayoutTransition
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
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
import com.hellguy39.domain.models.RadioStation
import com.hellguy39.domain.usecases.ConvertByteArrayToBitmapUseCase
import com.hellguy39.domain.usecases.state.SavedServiceStateUseCases
import com.hellguy39.domain.utils.PlayerType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var savedServiceStateUseCases: SavedServiceStateUseCases

    @Inject
    lateinit var convertByteArrayToBitmapUseCase: ConvertByteArrayToBitmapUseCase

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

        binding.trackCard.setOnClickListener {
            startActivity(Intent(this, TrackActivity::class.java))
        }

        binding.ibPlayPause.setOnClickListener {
            if (PlayerService.isPlaying().value == true) {
                PlayerService.onPause()
            } else {
                PlayerService.onPlay()
            }
        }

        setObservers()

        binding.layoutCardPlayer.visibility = View.GONE
        binding.layoutCardPlayer.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

        checkServiceSavedState()
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
                val type = PlayerService.getPlayerType().value

                if (type == PlayerType.LocalTrack) {

                    val metadata = PlayerService.getCurrentMetadata().value

                    if (metadata != null)
                        updateCardUIWithMetadata(metadata)

                    showTrackCard()
                } else if (type == PlayerType.Radio) {
                    updateCardUIWithRadioStation(PlayerService.getRadioStation())

                    showTrackCard()
                }
            } else {
                hideTrackCard()
            }
        }

        PlayerService.getCurrentMetadata().observe(this) { metadata ->
            val type = PlayerService.getPlayerType().value

            if (type == PlayerType.LocalTrack)
                updateCardUIWithMetadata(metadata)
        }
    }

    fun initSetupMediaLibraryDataViewModel() {
        mediaLibraryDataViewModel.initSetup()
    }


    private fun checkServiceSavedState() = CoroutineScope(Dispatchers.IO).launch {
        val state = savedServiceStateUseCases.getSavedServiceStateUseCase.invoke()

        withContext(Dispatchers.Main) {
            if (state.radioStation != null || state.playlist != null) {

                if(state.radioStation != null)
                    return@withContext //this is a temporary solution

                PlayerService.startService(
                    context = this@MainActivity,
                    contentWrapper = state
                )
            }
        }
    }

    private fun updateCardUIWithMetadata(metadata: MediaMetadata) {
        binding.tvTrackName.text = if (metadata.title.isNullOrEmpty()) "Unknown" else metadata.title
        binding.tvArtist.text  = if (metadata.artist.isNullOrEmpty()) "Unknown" else metadata.artist

        val bytes = metadata.artworkData

        if (bytes != null)
            binding.ivTrackImage.setImageBitmap(convertByteArrayToBitmapUseCase.invoke(bytes))
        else
            binding.ivTrackImage.setImageResource(R.drawable.ic_round_audiotrack_24)

    }

    private fun updateCardUIWithRadioStation(radioStation: RadioStation) {
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
}