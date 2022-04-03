package com.hellguy39.collapse.presentaton.activities.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.LayoutTransition
import android.content.Context
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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity()/*, NavController.OnDestinationChangedListener*/ {

    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var _binding: ActivityMainBinding

    private lateinit var mediaLibraryDataViewModel: MediaLibraryDataViewModel
    private lateinit var radioStationsDataViewModel: RadioStationsDataViewModel

    private var isNeedDisplayCard = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        mediaLibraryDataViewModel = ViewModelProvider(this)[MediaLibraryDataViewModel::class.java]
        radioStationsDataViewModel = ViewModelProvider(this)[RadioStationsDataViewModel::class.java]

        navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHostFragment.navController

        _binding.rootLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        NavigationUI.setupWithNavController(
            _binding.bottomNavigation,navController
        )

        _binding.trackCard.setOnClickListener {
            //navController.navigate(R.id.trackFragment)
            //hideTrackCard()
            startActivity(Intent(this, TrackActivity::class.java))
            //overridePendingTransition(R.anim.slide_up_in, R.anim.slide_up_out)
        }

        _binding.ibPlayPause.setOnClickListener {
            if (PlayerService.isPlaying().value == true) {
                PlayerService.onPause()
            } else {
                PlayerService.onPlay()
            }
        }

        setObservers()

        _binding.layoutCardPlayer.visibility = View.GONE
        _binding.layoutCardPlayer.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
    }

    private fun setObservers() {
        PlayerService.isPlaying().observe(this) {
            if (it) {
                _binding.ibPlayPause.setImageResource(R.drawable.ic_round_pause_24)
            } else {
                _binding.ibPlayPause.setImageResource(R.drawable.ic_round_play_arrow_24)
            }
        }

        PlayerService.getCurrentMetadata().observe(this) {

            if (it != null) {
                updateCardUI(it)
                isNeedDisplayCard = true
            }

            showTrackCard()

        }
    }

    fun initSetupMediaLibraryDataViewModel() {
        mediaLibraryDataViewModel.initSetup()
    }

    private fun updateCardUI(metadata: MediaMetadata) {
        _binding.tvTrackName.text = if (metadata.title.isNullOrEmpty()) "Unknown" else metadata.title
        _binding.tvArtist.text  = if (metadata.artist.isNullOrEmpty()) "Unknown" else metadata.artist

        val bytes = metadata.artworkData

        if (bytes != null)
            _binding.ivTrackImage.setImageBitmap(BitmapFactory.decodeByteArray(metadata.artworkData, 0, bytes.size))
        else
            _binding.ivTrackImage.setImageResource(R.drawable.ic_round_audiotrack_24)

    }

    private fun showTrackCard() {

        _binding.ibPlayPause.setImageResource(R.drawable.ic_round_pause_24)

        TransitionManager.beginDelayedTransition(_binding.layoutCardPlayer, AutoTransition())
        _binding.layoutCardPlayer.visibility = View.VISIBLE
    }

    private fun hideTrackCard() {
        TransitionManager.beginDelayedTransition(_binding.layoutCardPlayer, AutoTransition())
        _binding.layoutCardPlayer.visibility = View.GONE
    }
}