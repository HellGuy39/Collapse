package com.hellguy39.collapse.presentaton.activities.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.LayoutTransition
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.exoplayer2.MediaMetadata
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.ActivityMainBinding
import com.hellguy39.collapse.presentaton.services.PlayerService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {

    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var _binding: ActivityMainBinding

    private var isNeedDisplayCard = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHostFragment.navController

        _binding.rootLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        NavigationUI.setupWithNavController(_binding.bottomNavigation,navController)

        navController.addOnDestinationChangedListener(this)

        _binding.trackCard.visibility = View.INVISIBLE

        _binding.trackCard.setOnClickListener {
            navController.navigate(R.id.trackFragment)
            hideTrackCard()
        }

        _binding.ibPlayPause.setOnClickListener {
            if (PlayerService.isPlaying().value == true) {
                PlayerService.onPause()
                _binding.ibPlayPause.setImageResource(R.drawable.ic_round_play_arrow_24)
            } else {
                PlayerService.onPlay()
                _binding.ibPlayPause.setImageResource(R.drawable.ic_round_pause_24)
            }
        }

        setObservers()
    }

    private fun setObservers() {
        PlayerService.isPlaying().observe(this) {

        }

        PlayerService.getCurrentMetadata().observe(this) {

            if (it != null) {
                updateCardUI(it)
                isNeedDisplayCard = true
            }

            if (isNeedDisplayCard)
                if (_binding.trackCard.visibility == View.INVISIBLE)
                    showTrackCard()

        }
    }

    private fun updateCardUI(metadata: MediaMetadata) {
        _binding.tvTrackName.text = if (metadata.title.isNullOrEmpty()) "Unknown" else metadata.title
        _binding.tvArtist.text  = if (metadata.artist.isNullOrEmpty()) "Unknown" else metadata.artist
    }

    private fun showTrackCard() {
        _binding.ibPlayPause.setImageResource(R.drawable.ic_round_pause_24)
        _binding.trackCard.apply {
            translationY = this.height.toFloat()
            visibility = View.VISIBLE

            animate().translationY(0f)
                .setDuration(200)
                .setListener(null)
        }
    }

    private fun hideTrackCard() {
        _binding.trackCard.apply {
            translationY = 0f

            animate().translationY(this.height.toFloat())
                .setDuration(200)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        _binding.trackCard.visibility = View.INVISIBLE
                    }
                })
        }
    }

    private fun hideBottomNavigation() {
        TransitionManager.beginDelayedTransition(_binding.rootLayout, AutoTransition())
        _binding.bottomNavigation.visibility = View.GONE
    }

    private fun showBottomNavigation() {
        TransitionManager.beginDelayedTransition(_binding.rootLayout, AutoTransition())
        _binding.bottomNavigation.visibility = View.VISIBLE
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        if (isBottomNavigationFragment(destination.id)) {
            showBottomNavigation()
        } else {
            hideBottomNavigation()
        }
    }

    private fun isBottomNavigationFragment(id: Int):Boolean {
        return id == R.id.homeFragment ||
                id == R.id.mediaLibraryFragment ||
                id == R.id.radioFragment
    }
}