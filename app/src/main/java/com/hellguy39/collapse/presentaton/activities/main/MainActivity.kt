package com.hellguy39.collapse.presentaton.activities.main

import android.Manifest
import android.animation.LayoutTransition
import android.app.ActivityOptions
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.ActivityMainBinding
import com.hellguy39.collapse.presentaton.activities.track.TrackActivity
import com.hellguy39.collapse.presentaton.services.PlayerService
import com.hellguy39.collapse.presentaton.view_models.MediaLibraryDataViewModel
import com.hellguy39.collapse.presentaton.view_models.RadioStationsDataViewModel
import com.hellguy39.collapse.utils.formatForDisplaying
import com.hellguy39.collapse.utils.getColorByResId
import com.hellguy39.collapse.utils.toBitmap
import com.hellguy39.domain.models.RadioStation
import com.hellguy39.domain.usecases.app_settings.AppSettingsUseCases
import com.hellguy39.domain.utils.PlayerType
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), View.OnClickListener {

    @Inject
    lateinit var appSettingsUseCases: AppSettingsUseCases

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

        WindowCompat.setDecorFitsSystemWindows(window, false)

        initModels()
        initSetupMediaLibraryDataViewModel()

        setupBottomNavigation()

        binding.rootLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        binding.layoutCardPlayer.visibility = View.GONE
        binding.layoutCardPlayer.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

        setupListeners()

        setObservers()

        viewModel.checkServiceSavedState(this)
    }

    override fun onRestart() {
        super.onRestart()
        initSetupMediaLibraryDataViewModel()
    }

    private fun initModels() {
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        mediaLibraryDataViewModel = ViewModelProvider(this)[MediaLibraryDataViewModel::class.java]
        radioStationsDataViewModel = ViewModelProvider(this)[RadioStationsDataViewModel::class.java]
    }

    private fun setupBottomNavigation() {
        navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHostFragment.navController

        NavigationUI.setupWithNavController(
            binding.bottomNavigation,navController
        )
    }

    private fun setupListeners() {
        binding.trackCard.setOnClickListener(this)
        binding.ibPlayPause.setOnClickListener(this)
        binding.ibNextTrack.setOnClickListener(this)
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
            onServiceStateChanged()
        }

        PlayerService.getCurrentMetadata().observe(this) { metadata ->
            onServiceStateChanged()
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

    private fun onServiceStateChanged() {
        if (PlayerService.isRunningService().value == true) {
            updateCardUI(
                type = PlayerService.getServiceContent().type,
                metadata = PlayerService.getCurrentMetadata().value,
                radioStation = PlayerService.getServiceContent().radioStation,
            )
            showTrackCard()
        } else {
            hideTrackCard()
        }
    }

    fun initSetupMediaLibraryDataViewModel() {
        if (isPermissionGranted())
            mediaLibraryDataViewModel.initSetup()
    }

    private fun isPermissionGranted(): Boolean =
        (ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED)

    private fun updateCardUI(
        type: Enum<PlayerType>,
        metadata: MediaMetadata? = null,
        radioStation: RadioStation? = null
    ) {
        var bytes: ByteArray? = null

        when(type) {
            PlayerType.LocalTrack -> {
                if (metadata != null) {
                    binding.ibNextTrack.visibility = View.VISIBLE

                    binding.tvTrackName.text = metadata.title.formatForDisplaying()

                    binding.tvArtist.text = metadata.artist.formatForDisplaying()

                    bytes = metadata.artworkData
                }
            }
            PlayerType.Radio -> {
                if (radioStation != null) {
                    binding.ibNextTrack.visibility = View.GONE

                    binding.tvTrackName.text = radioStation.name.formatForDisplaying()
                    binding.tvArtist.text = ""

                    bytes = radioStation.picture
                }
            }
            else -> {

            }
        }

        Glide.with(this)
            .load(bytes)
            .placeholder(
                if (type == PlayerType.Radio)
                    R.drawable.ic_round_radio_24
                else
                    R.drawable.ic_round_audiotrack_24
            )
            .apply(RequestOptions.bitmapTransform(RoundedCorners(16)))
            .into(binding.ivTrackImage)

        if (bytes != null && appSettingsUseCases.getAppSettingsUseCase.invoke().isAdaptableBackgroundEnabled) {
            setCustomCardColorScheme(bitmap = bytes.toBitmap())
        } else {
            setDefaultCardColorScheme()
        }
    }

    private fun setCustomCardColorScheme(bitmap: Bitmap) {
        val white = ResourcesCompat.getColor(resources, R.color.white,null)

        Palette.from(bitmap).generate { palette ->
            if (palette != null) {
                binding.trackCard.backgroundTintList = ColorStateList.valueOf(palette.getMutedColor(1))
                binding.tvTrackName.setTextColor(white)
                binding.tvArtist.setTextColor(white)
                binding.ibPlayPause.imageTintList = ColorStateList.valueOf(white)
                binding.ibNextTrack.imageTintList = ColorStateList.valueOf(white)
            } else {
                setDefaultCardColorScheme()
            }
        }
    }

    private fun setDefaultCardColorScheme() {
        val colorOnSurface = theme.getColorByResId(com.google.android.material.R.attr.colorOnSurface)

        binding.trackCard.backgroundTintList = null

        binding.ibPlayPause.imageTintList = ColorStateList.valueOf(colorOnSurface)
        binding.ibNextTrack.imageTintList = ColorStateList.valueOf(colorOnSurface)

        binding.tvTrackName.setTextColor(colorOnSurface)
        binding.tvArtist.setTextColor(colorOnSurface)
    }

    private fun showTrackCard() {
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