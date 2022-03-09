package com.hellguy39.collapse.presentaton.activities.main

import android.animation.LayoutTransition
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.hellguy39.collapse.R
import com.hellguy39.collapse.app.App
import com.hellguy39.collapse.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {

    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var _binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        //(applicationContext as App).appComponent.inject(this)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHostFragment.navController

        _binding.rootLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        NavigationUI.setupWithNavController(_binding.bottomNavigation,navController)

        navController.addOnDestinationChangedListener(this)
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
        if (destination.id == R.id.homeFragment || destination.id == R.id.mediaLibraryFragment) {
            showBottomNavigation()
        } else {
            hideBottomNavigation()
        }
    }
}