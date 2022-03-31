package com.hellguy39.collapse.presentaton.activities.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity()/*, NavController.OnDestinationChangedListener*/ {

    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var _binding: ActivityMainBinding

    private lateinit var mediaLibraryDataViewModel: MediaLibraryDataViewModel

    private var isNeedDisplayCard = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        mediaLibraryDataViewModel = ViewModelProvider(this)[MediaLibraryDataViewModel::class.java]

        navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHostFragment.navController

        _binding.rootLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        NavigationUI.setupWithNavController(
            _binding.bottomNavigation,navController
        )


        //navController.addOnDestinationChangedListener(this)

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
//        val id = navController.currentDestination?.id ?: return

//        if(!isBottomNavigationFragment(id))
//            return

        _binding.ibPlayPause.setImageResource(R.drawable.ic_round_pause_24)
        /*_binding.trackCard.apply {
            translationY = this.height.toFloat()
            visibility = View.VISIBLE

            animate().translationY(0f)
                .setDuration(200)
                .setListener(null)
        }*/
        TransitionManager.beginDelayedTransition(_binding.layoutCardPlayer, AutoTransition())
        _binding.layoutCardPlayer.visibility = View.VISIBLE
    }

    private fun hideTrackCard() {
        _binding.trackCard.apply {
            translationY = 0f
            animate().translationY(this.height.toFloat())
                .setDuration(0)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        _binding.trackCard.visibility = View.INVISIBLE
                    }
                })
        }
    }

//    private fun hideBottomNavigation() {
//        TransitionManager.beginDelayedTransition(_binding.rootLayout, AutoTransition())
//        _binding.bottomNavigation.visibility = View.GONE
//    }
//
//    private fun showBottomNavigation() {
//        TransitionManager.beginDelayedTransition(_binding.rootLayout, AutoTransition())
//        _binding.bottomNavigation.visibility = View.VISIBLE
//    }

//    override fun onDestinationChanged(
//        controller: NavController,
//        destination: NavDestination,
//        arguments: Bundle?
//    ) {
//        if (isBottomNavigationFragment(destination.id)) {
//            showBottomNavigation()
//            checkTrackCard(true)
//        } else {
//            hideBottomNavigation()
//            checkTrackCard(false)
//        }
//    }

//    private fun checkTrackCard(enable: Boolean) {
//        if (enable) {
//            if (isNeedDisplayCard) {
//                if (_binding.trackCard.visibility == View.INVISIBLE) {
//                    showTrackCard()
//                }
//            }
//        } else {
//            if (!isNeedDisplayCard) {
//                if (_binding.trackCard.visibility == View.VISIBLE) {
//                    hideTrackCard()
//                }
//            }
//        }
//    }
//
//    private fun isBottomNavigationFragment(id: Int):Boolean {
//        return id == R.id.homeFragment ||
//                id == R.id.mediaLibraryFragment ||
//                id == R.id.radioFragment ||
//                id == R.id.equalizerFragment
//    }
}