package com.hellguy39.collapse.presentaton.activities.main

import android.animation.LayoutTransition
import android.app.ActivityOptions
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
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
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.ActivityMainBinding
import com.hellguy39.collapse.presentaton.activities.track.TrackActivity
import com.hellguy39.collapse.presentaton.services.PlayerService
import com.hellguy39.collapse.presentaton.view_models.MediaLibraryDataViewModel
import com.hellguy39.collapse.presentaton.view_models.RadioStationsDataViewModel
import com.hellguy39.domain.models.RadioStation
import com.hellguy39.domain.usecases.ConvertByteArrayToBitmapUseCase
import com.hellguy39.domain.usecases.GetColorFromThemeUseCase
import com.hellguy39.domain.utils.PlayerType
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), View.OnClickListener {

    @Inject
    lateinit var convertByteArrayToBitmapUseCase: ConvertByteArrayToBitmapUseCase

    @Inject
    lateinit var getColorFromThemeUseCase: GetColorFromThemeUseCase

    private lateinit var primaryColorStateList: ColorStateList
    private lateinit var onSurfaceColorStateList: ColorStateList
    @ColorInt private var colorSurface: Int = 0
    @ColorInt private var textColor: Int = 0
    @ColorInt private var colorPrimary = 0
    @ColorInt private var white = 0
    @ColorInt private var colorOnSurface = 0
    private lateinit var defIconTint: ColorStateList

    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var binding: ActivityMainBinding

    private lateinit var mediaLibraryDataViewModel: MediaLibraryDataViewModel
    private lateinit var radioStationsDataViewModel: RadioStationsDataViewModel
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        window.sharedElementsUseOverlay = false
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initColors()

        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
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

        viewModel.checkServiceSavedState(this)
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
                binding.ibPlayPause.setImageResource(R.drawable.ic_round_play_arrow_48)
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

        PlayerService.getCurrentDuration().observe(this) {
            if (PlayerService.isRunningService().value == true) {
                val duration = PlayerService.getDuration()

                if (duration < 0 || it < 0)
                    return@observe

                binding.trackProgressIndicator.max = duration.toInt()
                binding.trackProgressIndicator.progress = it.toInt()
            }
        }
    }

    fun initSetupMediaLibraryDataViewModel() {
        mediaLibraryDataViewModel.initSetup()
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
            val bitmap = convertByteArrayToBitmapUseCase.invoke(bytes)
            binding.ivTrackImage.setImageBitmap(bitmap)
            updatePalette(bitmap)
            binding.tvTrackName.setTextColor(white)
            binding.tvArtist.setTextColor(white)
            binding.ibPlayPause.imageTintList = ColorStateList.valueOf(white)
            binding.ibNextTrack.imageTintList = ColorStateList.valueOf(white)
        } else {
            binding.ivTrackImage.setImageResource(R.drawable.ic_round_audiotrack_24)
            binding.trackCard.backgroundTintList = null
            binding.ibPlayPause.imageTintList = onSurfaceColorStateList
            binding.ibNextTrack.imageTintList = onSurfaceColorStateList
            binding.tvTrackName.setTextColor(colorOnSurface)
            binding.tvArtist.setTextColor(colorOnSurface)
        }

    }

    private fun initColors() {
        white = ResourcesCompat.getColor(resources, R.color.white,null)
        colorPrimary = getColorFromThemeUseCase.invoke(theme, com.google.android.material.R.attr.colorPrimary)
        colorOnSurface = getColorFromThemeUseCase.invoke(theme, com.google.android.material.R.attr.colorOnSurface)
        primaryColorStateList = ColorStateList.valueOf(colorPrimary)
        onSurfaceColorStateList = ColorStateList.valueOf(colorOnSurface)

        textColor = binding.tvTrackName.currentTextColor
        defIconTint = binding.ibPlayPause.imageTintList ?: ColorStateList.valueOf(colorOnSurface)
        colorSurface = getColorFromThemeUseCase.invoke(theme, com.google.android.material.R.attr.colorSurface)
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
            val bitmap = convertByteArrayToBitmapUseCase.invoke(bytes)
            binding.ivTrackImage.setImageBitmap(bitmap)
            updatePalette(bitmap)
            binding.tvTrackName.setTextColor(white)
            binding.tvArtist.setTextColor(white)
            binding.ibPlayPause.imageTintList = ColorStateList.valueOf(white)
            binding.ibNextTrack.imageTintList = ColorStateList.valueOf(white)
        } else {
            binding.ivTrackImage.setImageResource(R.drawable.ic_round_radio_24)
            binding.trackCard.backgroundTintList = null
            binding.trackCard.backgroundTintList = null
            binding.ibPlayPause.imageTintList = onSurfaceColorStateList
            binding.ibNextTrack.imageTintList = onSurfaceColorStateList
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

        binding.trackProgressIndicator.visibility = View.VISIBLE
    }

    private fun hideTrackCard() {
        TransitionManager.beginDelayedTransition(binding.layoutCardPlayer, AutoTransition())
        binding.layoutCardPlayer.visibility = View.GONE

        binding.trackProgressIndicator.visibility = View.GONE
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            binding.trackCard.id -> {
                val options = ActivityOptions.makeSceneTransitionAnimation(
                    this,
                    binding.trackCard,
                    "track_card_transition"
                )
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