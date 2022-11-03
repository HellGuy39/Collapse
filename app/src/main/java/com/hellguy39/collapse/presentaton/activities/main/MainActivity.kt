package com.hellguy39.collapse.presentaton.activities.main

import android.Manifest.permission.READ_MEDIA_AUDIO
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.card.MaterialCardView
import com.hellguy39.collapse.R
import com.hellguy39.collapse.albums.list.AlbumListViewModel
import com.hellguy39.collapse.artists.list.ArtistListViewModel
import com.hellguy39.collapse.core.ui.extensions.doOnApplyWindowInsets
import com.hellguy39.collapse.core.ui.extensions.repeatOnLifecycleStarted
import com.hellguy39.collapse.databinding.ActivityMainBinding
import com.hellguy39.collapse.mediaplayer.MediaViewModel
import com.hellguy39.collapse.mediaplayer.PlaybackBottomSheet
import com.hellguy39.collapse.songs.ui.SongFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val songsViewModel: SongFragmentViewModel by viewModels()
    private val artistListViewModel: ArtistListViewModel by viewModels()
    private val albumListViewModel: AlbumListViewModel by viewModels()
    private val mediaViewModel: MediaViewModel by viewModels()

    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { permission ->
        if (permission == true) {
            songsViewModel.reloadData()
            artistListViewModel.reloadData()
            albumListViewModel.reloadData()
        }
    }

    private fun isPermissionGranted(
        permission: String
    ): Boolean = ActivityCompat
        .checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    private var binding: ActivityMainBinding? = null

    private var bottomSheet: BottomSheetBehavior<MaterialCardView>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        //window.sharedElementsUseOverlay = false
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        if (!isPermissionGranted(READ_MEDIA_AUDIO)) {
            requestPermission.launch(READ_MEDIA_AUDIO)
        }

        binding?.run {
            bottomNavigation.doOnApplyWindowInsets { view, windowInsetsCompat, rect ->
                view.updatePadding(
                    bottom = rect.bottom + windowInsetsCompat.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
                )
                windowInsetsCompat
            }
//            bottomSheetPlayback.root.doOnApplyWindowInsets { view, windowInsetsCompat, rect ->
//                (view as CardView).setContentPadding(
//                    0,
//                    0,//rect.top + windowInsetsCompat.getInsets(WindowInsetsCompat.Type.systemBars()).top,
//                    0,
//                    0//rect.bottom + windowInsetsCompat.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
//                )
//                windowInsetsCompat
//            }
        }

        binding?.run {
            bottomSheet = BottomSheetBehavior.from(bottomSheetPlayback.root)
        }

        repeatOnLifecycleStarted {
            launch {
                mediaViewModel.song.collect {
                    if (it != null) {
                        binding?.bottomSheetPlayback?.run {
                            tvSongName.text = it.title
                        }
                    }
                }
            }
        }
//        val bottomSheet = BottomSheetBehavior.from(binding?.bottomSheetPlayback)
//
        binding?.bottomSheetPlayback?.run {
            PlaybackBottomSheet(root)
            btnPlayPause.setOnClickListener {

            }
        }

        setupBottomNavigation()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun setupBottomNavigation() {
        navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { controller, destination, arguments ->

            if (destination.id == com.hellguy39.collapse.artists.R.id.artistDetailFragment) {
                binding?.run {
                    if (bottomNavigation.visibility != View.GONE) {
                        bottomNavigation.visibility = View.GONE
                    }
                }
            } else {
                binding?.run {
                    if (bottomNavigation.visibility != View.VISIBLE) {
                        bottomNavigation.visibility = View.VISIBLE
                    }
                }
            }

        }

        binding?.let {
            NavigationUI.setupWithNavController(
                it.bottomNavigation,navController
            )
        }
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            else -> Unit
        }
    }

}