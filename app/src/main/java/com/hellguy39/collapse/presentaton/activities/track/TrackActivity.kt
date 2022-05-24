package com.hellguy39.collapse.presentaton.activities.track

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.Player
import com.google.android.material.slider.Slider
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.ActivityTrackBinding
import com.hellguy39.collapse.presentaton.fragments.trackMenuBottomSheet.TrackMenuBottomSheet
import com.hellguy39.collapse.presentaton.fragments.track_list.TrackMenuEvents
import com.hellguy39.collapse.presentaton.services.PlayerService
import com.hellguy39.collapse.utils.*
import com.hellguy39.collapse.utils.formatAsDate
import com.hellguy39.collapse.utils.getColorByResId
import com.hellguy39.collapse.utils.setOnBackActivityNavigation
import com.hellguy39.collapse.utils.toBitmap
import com.hellguy39.domain.models.Track
import com.hellguy39.domain.usecases.app_settings.AppSettingsUseCases
import com.hellguy39.domain.usecases.favourites.FavouriteTracksUseCases
import com.hellguy39.domain.utils.PlayerType
import com.hellguy39.domain.utils.PlaylistType
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TrackActivity : AppCompatActivity(),
    View.OnClickListener,
    TrackMenuEvents,
    Slider.OnChangeListener,
    Slider.OnSliderTouchListener{

    @Inject
    lateinit var favouriteTracksUseCases: FavouriteTracksUseCases

    @Inject
    lateinit var appSettingsUseCases: AppSettingsUseCases

    private lateinit var binding: ActivityTrackBinding

    private lateinit var viewModel: TrackActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        binding = ActivityTrackBinding.inflate(layoutInflater)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        viewModel = ViewModelProvider(this)[TrackActivityViewModel::class.java]

        window.sharedElementEnterTransition = MaterialContainerTransform().apply {
            addTarget(binding.root)
            setAllContainerColors(theme.getColorByResId(com.google.android.material.R.attr.colorSurface))
            duration = 300L
        }
        window.sharedElementReturnTransition = MaterialContainerTransform().apply {
            addTarget(binding.root)
            setAllContainerColors(theme.getColorByResId(com.google.android.material.R.attr.colorSurface))
            duration = 250L
        }

        setContentView(binding.root)

        binding.topAppBar.setOnBackActivityNavigation(this)

        binding.ibPlayPause.setOnClickListener(this)
        binding.ibNextTrack.setOnClickListener(this)
        binding.ibPreviousTrack.setOnClickListener(this)
        binding.ibShuffle.setOnClickListener(this)
        binding.ibRepeatTrack.setOnClickListener(this)

        binding.topAppBar.menu.findItem(R.id.more).setOnMenuItemClickListener {
            val bottomSheet = TrackMenuBottomSheet(
                track = PlayerService.getCurrentTrack() ?: Track(),
                position = PlayerService.getContentPosition().value ?: 0,
                listener = this,
                playlistType = PlayerService.getServiceContent().playlist?.type ?: PlaylistType.Undefined,
                isTrackFavouriteUseCase = favouriteTracksUseCases.isTrackFavouriteUseCase,
            )
            bottomSheet.show(supportFragmentManager, TrackMenuBottomSheet.TRACK_MENU_TAG)
            true
        }

        setupTimeSlider()
        setupVolumeSlider()
        setDeviceVolumeObserver()

        when(PlayerService.getServiceContent().type) {
            PlayerType.Radio -> {
                setupForUIForRadioStation()
            }
            PlayerType.LocalTrack -> {
                setupUIForLocalTrack()
            }
            else -> {

            }
        }
    }

    private fun setupTimeSlider() {
        binding.sliderTime.addOnChangeListener { slider, value, fromUser ->
            if (fromUser)
                PlayerService.onSeekTo(value.toLong())
        }
    }

    private fun setupForUIForRadioStation() {
        binding.topAppBar.menu.clear()
        binding.sliderTime.visibility = View.GONE
        binding.tvCurrentTrackTime.visibility = View.GONE
        binding.tvRemainingTrackTime.visibility = View.GONE
        binding.ibNextTrack.visibility = View.GONE
        binding.ibPreviousTrack.visibility = View.GONE
        binding.ibShuffle.visibility = View.GONE
        binding.ibRepeatTrack.visibility = View.GONE

        setIsPlayingObserver()

        val bytes = PlayerService.getServiceContent().radioStation?.picture
        setupImage(bytes)
        binding.tvTrackName.text = PlayerService.getServiceContent().radioStation?.name.formatForDisplaying()
    }

    private fun setupImage(bytes: ByteArray?) {

        Glide.with(this)
            .load(bytes)
            .placeholder(
                if (PlayerService.getServiceContent().type == PlayerType.Radio)
                    R.drawable.ic_round_radio_24
                else
                    R.drawable.ic_round_audiotrack_24
            )
            .into(binding.ivCover)

        if (bytes.toBitmap() != null && appSettingsUseCases.getAppSettingsUseCase.invoke().isAdaptableBackgroundEnabled) {
            updatePalette(bytes.toBitmap()!!)
        } else {
            setDefaultUIColors()
        }
    }

    private fun setupUIForLocalTrack() {
        setCurrentMetadataObserver()
        setIsPlayingObserver()
        setShuffleObserver()
        setRepeatModeObserver()
        setTimelineObserver()
    }

    private fun setupVolumeSlider() {
        binding.sliderVolume.addOnSliderTouchListener(this)
        binding.sliderVolume.addOnChangeListener(this)

        val deviceInfo = PlayerService.getDeviceInfo()
        val maxVolume = deviceInfo.maxVolume
        val minVolume = deviceInfo.minVolume

        binding.sliderVolume.apply {
            valueTo = maxVolume.toFloat()
            valueFrom = minVolume.toFloat()
            stepSize = 1f
        }
    }

    private fun setDeviceVolumeObserver() = PlayerService.getDeviceVolume().observe(this) {
        binding.sliderVolume.value = it.toFloat()
    }

    private fun setIsPlayingObserver() = PlayerService.isPlaying().observe(this) {
        if (it) {
            binding.ibPlayPause.setImageResource(R.drawable.ic_baseline_pause_circle_filled_64)
        } else {
            binding.ibPlayPause.setImageResource(R.drawable.ic_baseline_play_circle_filled_64)
        }
    }

    private fun setCurrentMetadataObserver() = PlayerService.getCurrentMetadata().observe(this) {
        if (it != null)
            updateUI(it)
    }

    private fun setRepeatModeObserver() = PlayerService.getRepeatMode().observe(this) {
        updateRepeatMode(it)
    }

    private fun setShuffleObserver() = PlayerService.isShuffle().observe(this) {
        updateShuffle(it)
    }

    private fun setTimelineObserver() = PlayerService.getCurrentDuration().observe(this) {

        val duration = PlayerService.getDuration()

        if (duration < 0 || it < 0)
            return@observe

        binding.sliderTime.apply {
            valueFrom = 0f
            valueTo = (duration).toFloat()
            value = (it).toFloat()
        }

        binding.tvCurrentTrackTime.text = it.formatAsDate()
        binding.tvRemainingTrackTime.text = (duration - it).formatAsDate()

    }

    private fun updateShuffle(b: Boolean) {
        if (b) {
            binding.ibShuffle.imageTintList = ColorStateList.valueOf(
                theme.getColorByResId(com.google.android.material.R.attr.colorPrimary)
            )
        } else {
            binding.ibShuffle.imageTintList = ColorStateList.valueOf(
                theme.getColorByResId(com.google.android.material.R.attr.colorOnSurface)
            )
        }
    }

    private fun updateRepeatMode(repeatMode: Int) {
        val primaryColorStateList = ColorStateList.valueOf(theme.getColorByResId(com.google.android.material.R.attr.colorOnSurface))
        val onSurfaceColorStateList = ColorStateList.valueOf(theme.getColorByResId(com.google.android.material.R.attr.colorPrimary))
        
        when (repeatMode) {
            Player.REPEAT_MODE_ALL -> {
                binding.ibRepeatTrack.imageTintList = primaryColorStateList
                binding.ibRepeatTrack.setImageResource(R.drawable.ic_round_repeat_24)
            }
            Player.REPEAT_MODE_ONE -> {
                binding.ibRepeatTrack.imageTintList = primaryColorStateList
                binding.ibRepeatTrack.setImageResource(R.drawable.ic_round_repeat_one_24)
            }
            else -> {
                binding.ibRepeatTrack.imageTintList = onSurfaceColorStateList
                binding.ibRepeatTrack.setImageResource(R.drawable.ic_round_repeat_24)
            }
        }
    }

    private fun updateUI(mediaMetadata: MediaMetadata) {

        binding.sliderTime.setLabelFormatter {
            (it.toLong()).formatAsDate()
        }

        binding.topAppBar.title = PlayerService.getServiceContent().playlist?.name

        binding.tvTrackName.text = mediaMetadata.title.formatForDisplaying()
        binding.tvPerformer.text = mediaMetadata.artist.formatForDisplaying()

        val bytes = mediaMetadata.artworkData

        setupImage(bytes)
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            binding.ibPlayPause.id -> {
                if (PlayerService.isPlaying().value == true)
                    PlayerService.onPause()
                else
                    PlayerService.onPlay()
            }
            binding.ibNextTrack.id -> {
                PlayerService.onNext()
            }
            binding.ibPreviousTrack.id -> {
                PlayerService.onPrevious()
            }
            binding.ibShuffle.id -> {
                if (PlayerService.isShuffle().value == true)
                    PlayerService.onShuffle(false)
                else
                    PlayerService.onShuffle(true)
            }
            binding.ibRepeatTrack.id -> {
                when {
                    PlayerService.isRepeat() == Player.REPEAT_MODE_OFF -> PlayerService.onRepeat(Player.REPEAT_MODE_ALL)
                    PlayerService.isRepeat() == Player.REPEAT_MODE_ALL -> PlayerService.onRepeat(Player.REPEAT_MODE_ONE)
                    else -> PlayerService.onRepeat(Player.REPEAT_MODE_OFF)
                }
            }
        }
    }

    private fun updatePalette(bitmap: Bitmap) {
        Palette.from(bitmap).generate { palette ->
            if (palette != null) {
                val mutedColor = palette.getMutedColor(1)
                //val darkMutedColor = palette.getDarkMutedColor(1)
                val white = ResourcesCompat.getColor(resources, R.color.white,null)
                //val stateListMuted = ColorStateList.valueOf(mutedColor)
                val stateListWhite = ColorStateList.valueOf(white)

                binding.topAppBar.setNavigationIconTint(white)
                binding.topAppBar.setTitleTextColor(white)
                binding.tvTrackName.setTextColor(white)
                binding.tvPerformer.setTextColor(white)
                binding.tvRemainingTrackTime.setTextColor(white)
                binding.tvCurrentTrackTime.setTextColor(white)
                binding.ibPlayPause.imageTintList = stateListWhite
                binding.ibNextTrack.imageTintList = stateListWhite
                binding.ibPreviousTrack.imageTintList = stateListWhite
                binding.topAppBar.menu.findItem(R.id.more).iconTintList = stateListWhite

                binding.topAppBar.setBackgroundColor(mutedColor)
                binding.root.setBackgroundColor(mutedColor)

            }
        }
    }

    private fun setDefaultUIColors() {
        theme.getColorByResId(com.google.android.material.R.attr.colorOnSurface).also { colorOnSurface ->
            binding.topAppBar.setTitleTextColor(colorOnSurface)
            binding.tvTrackName.setTextColor(colorOnSurface)
            binding.tvPerformer.setTextColor(colorOnSurface)
            binding.tvRemainingTrackTime.setTextColor(colorOnSurface)
            binding.tvCurrentTrackTime.setTextColor(colorOnSurface)
            binding.topAppBar.setNavigationIconTint(colorOnSurface)

            ColorStateList.valueOf(colorOnSurface).also { colorStateList ->
                binding.ibPlayPause.imageTintList = colorStateList
                binding.ibNextTrack.imageTintList = colorStateList
                binding.ibPreviousTrack.imageTintList = colorStateList
                binding.topAppBar.menu.findItem(R.id.more).iconTintList = colorStateList
            }
        }
        theme.getColorByResId(com.google.android.material.R.attr.colorSurface).also { colorSurface ->
            binding.topAppBar.setBackgroundColor(colorSurface)
            binding.root.setBackgroundColor(colorSurface)
        }
    }

    @SuppressLint("RestrictedApi")
    override fun onValueChange(slider: Slider, value: Float, fromUser: Boolean) {
        if (fromUser)
            PlayerService.setDeviceVolume(value.toInt())
    }

    @SuppressLint("RestrictedApi")
    override fun onStartTrackingTouch(slider: Slider) {
        PlayerService.setDeviceVolume(slider.value.toInt())
    }

    @SuppressLint("RestrictedApi")
    override fun onStopTrackingTouch(slider: Slider) {

    }

    override fun onAddToFavourites(track: Track) {
        viewModel.addToFavourites(track)
    }

    override fun onDeleteFromFavourites(track: Track) {
        viewModel.deleteFromFavourites(track)
    }

    override fun onDeleteFromPlaylist(track: Track, position: Int) {
        viewModel.deleteFromPlaylist(track, position)
    }

}