package com.hellguy39.collapse.presentaton.activities.track

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.os.Bundle
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.MenuRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.res.ResourcesCompat
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.Player
import com.google.android.material.slider.Slider
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.ActivityTrackBinding
import com.hellguy39.collapse.presentaton.adapters.TracksAdapter
import com.hellguy39.collapse.presentaton.services.PlayerService
import com.hellguy39.domain.models.Track
import com.hellguy39.domain.usecases.ConvertByteArrayToBitmapUseCase
import com.hellguy39.domain.usecases.favourites.FavouriteTracksUseCases
import com.hellguy39.domain.usecases.playlist.PlaylistUseCases
import com.hellguy39.domain.utils.PlaylistType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class TrackActivity : AppCompatActivity(),
    View.OnClickListener,
    TracksAdapter.OnTrackListener,
    Slider.OnChangeListener,
    Slider.OnSliderTouchListener{

    @Inject
    lateinit var playlistUseCases: PlaylistUseCases

    @Inject
    lateinit var favouriteTracksUseCase: FavouriteTracksUseCases

    @Inject
    lateinit var convertByteArrayToBitmapUseCase: ConvertByteArrayToBitmapUseCase

    private lateinit var binding: ActivityTrackBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.topAppBar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.ibPlayPause.setImageResource(R.drawable.ic_round_pause_24)

        binding.ibPlayPause.setOnClickListener(this)
        binding.ibNextTrack.setOnClickListener(this)
        binding.ibPreviousTrack.setOnClickListener(this)
        binding.ibShuffle.setOnClickListener(this)
        binding.ibRepeatTrack.setOnClickListener(this)
        binding.ibMore.setOnClickListener(this)

        binding.sliderTime.addOnChangeListener { slider, value, fromUser ->
            if (fromUser)
                PlayerService.onSeekTo((value * 1000).toLong())
        }

        setObservers()

        binding.sliderVolume.value = PlayerService.getDeviceVolume().toFloat()
        binding.sliderVolume.addOnSliderTouchListener(this)
        binding.sliderVolume.addOnChangeListener(this)
    }


    private fun setObservers() {
        PlayerService.isPlaying().observe(this) {
            if (it) {
                binding.ibPlayPause.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24)
            } else {
                binding.ibPlayPause.setImageResource(R.drawable.ic_baseline_play_circle_filled_24)
            }
        }

        PlayerService.getCurrentMetadata().observe(this) {
            if (it != null)
                updateUI(it)
        }

        PlayerService.getRepeatMode().observe(this) {
            when (it) {
                Player.REPEAT_MODE_ALL -> {
                    val typedValue = TypedValue()
                    val theme = this.theme
                    theme.resolveAttribute(com.google.android.material.R.attr.colorPrimary, typedValue, true)
                    @ColorInt val colorPrimary = typedValue.data
                    binding.ibRepeatTrack.imageTintList = ColorStateList.valueOf(colorPrimary)
                    binding.ibRepeatTrack.setImageResource(R.drawable.ic_round_repeat_24)
                }
                Player.REPEAT_MODE_ONE -> {
                    val typedValue = TypedValue()
                    val theme = this.theme
                    theme.resolveAttribute(com.google.android.material.R.attr.colorPrimary, typedValue, true)
                    @ColorInt val colorPrimary = typedValue.data
                    binding.ibRepeatTrack.imageTintList = ColorStateList.valueOf(colorPrimary)
                    binding.ibRepeatTrack.setImageResource(R.drawable.ic_round_repeat_one_24)
                }
                else -> {
                    val typedValue = TypedValue()
                    val theme = this.theme
                    theme.resolveAttribute(com.google.android.material.R.attr.colorOnSurface, typedValue, true)
                    @ColorInt val colorOnSurface = typedValue.data
                    binding.ibRepeatTrack.imageTintList = ColorStateList.valueOf(colorOnSurface)
                    binding.ibRepeatTrack.setImageResource(R.drawable.ic_round_repeat_24)
                }
            }
        }

        PlayerService.isShuffle().observe(this) {
            if (it) {
                val typedValue = TypedValue()
                val theme = this.theme
                theme.resolveAttribute(com.google.android.material.R.attr.colorPrimary, typedValue, true)
                @ColorInt val colorPrimary = typedValue.data
                binding.ibShuffle.imageTintList = ColorStateList.valueOf(colorPrimary)
            } else {
                val typedValue = TypedValue()
                val theme = this.theme
                theme.resolveAttribute(com.google.android.material.R.attr.colorOnSurface, typedValue, true)
                @ColorInt val colorOnSurface = typedValue.data
                binding.ibShuffle.imageTintList = ColorStateList.valueOf(colorOnSurface)
            }
        }

    }

    private fun updateUI(mediaMetadata: MediaMetadata) {

        CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                val duration = PlayerService.getDuration() / 1000
                val position = PlayerService.getCurrentPosition() / 1000

                if (duration > 0 || position > 0) {
                    binding.sliderTime.apply {
                        valueFrom = 0f
                        valueTo = (duration).toFloat()
                        value = (position).toFloat()
                    }
                    binding.tvCurrentTrackTime.text = SimpleDateFormat("m:ss", Locale.getDefault()).format(Date(position * 1000))
                    binding.tvRemainingTrackTime.text = SimpleDateFormat("m:ss", Locale.getDefault())
                        .format(Date((duration - position) * 1000))
                }
                delay(500)
            }
        }

        binding.sliderTime.setLabelFormatter {
            SimpleDateFormat("m:ss", Locale.getDefault()).format(Date(it.toLong() * 1000))
        }

        binding.topAppBar.title = PlayerService.getServiceContent().playlist?.name

        binding.tvTrackName.text = mediaMetadata.title ?: "Unknown"
        binding.tvPerformer.text = mediaMetadata.artist ?: "Unknown"

        val bytes = mediaMetadata.artworkData

        if (bytes != null) {
            val bitmap = convertByteArrayToBitmapUseCase.invoke(bytes)
            Glide.with(this).load(bitmap).into(binding.ivCover)
            updatePalette(bitmap)
        } else
            binding.ivCover.setImageResource(R.drawable.ic_round_audiotrack_24)
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
            binding.ibMore.id -> {
                showMenu(
                    v = binding.ibMore,
                    menuRes = R.menu.track_item_menu,
                    listener = this,
                    context = this,
                    track = PlayerService.getCurrentTrack() ?: return,
                    type = PlayerService.getServiceContent().playlist?.type ?: PlaylistType.Undefined,
                    position = PlayerService.getServiceContent().position
                    )
                }
            }
        }

    private fun updatePalette(bitmap: Bitmap) {
        Palette.from(bitmap).generate { palette ->
            if (palette != null) {
                val white = ResourcesCompat.getColor(resources, R.color.white,null)

                binding.topAppBar.setNavigationIconTint(white)
                binding.topAppBar.setTitleTextColor(white)
                binding.topAppBar.setBackgroundColor(palette.getMutedColor(1))
                binding.root.setBackgroundColor(palette.getMutedColor(1))
                binding.tvTrackName.setTextColor(white)
                binding.tvPerformer.setTextColor(white)
                binding.tvRemainingTrackTime.setTextColor(white)
                binding.tvCurrentTrackTime.setTextColor(white)

                binding.ibPlayPause.imageTintList = ColorStateList.valueOf(white)
                binding.ibNextTrack.imageTintList = ColorStateList.valueOf(white)
                binding.ibPreviousTrack.imageTintList = ColorStateList.valueOf(white)

            }
        }
    }

    override fun onTrackClick(track: Track, position: Int) {

    }

    override fun onAddToFavourites(track: Track) {
        CoroutineScope(Dispatchers.IO).launch {
            favouriteTracksUseCase.addFavouriteTrackUseCase.invoke(track)
        }
    }

    override fun onDeleteFromPlaylist(track: Track, position: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val playlist = PlayerService.getServiceContent().playlist
            val updatedList = mutableListOf<Track>()

            if (playlist == null)
                return@launch

            updatedList.addAll(playlist.tracks)
            updatedList.remove(track)
            playlist.tracks = updatedList
            playlistUseCases.updatePlaylistUseCase.invoke(playlist)
        }
    }


    private fun showMenu(
        v: View,
        context: Context,
        @MenuRes menuRes: Int,
        listener: TracksAdapter.OnTrackListener,
        track: Track,
        type: Enum<PlaylistType>,
        position: Int
    ) {
        val popup = PopupMenu(context, v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            when(menuItem.itemId) {
                R.id.deleteFromPlaylist -> {
                    listener.onDeleteFromPlaylist(track = track, position = position)
                    true
                }
                R.id.addToFavourites -> {
                    listener.onAddToFavourites(track = track)
                    true
                }
                else -> false
            }
        }

        when(type) {
            PlaylistType.Custom -> { }
            PlaylistType.Favourites -> {
                popup.menu.findItem(R.id.addToFavourites).isVisible = false
            }
            PlaylistType.AllTracks -> {
                popup.menu.findItem(R.id.deleteFromPlaylist).isVisible = false
            }
            PlaylistType.Artist -> {
                popup.menu.findItem(R.id.deleteFromPlaylist).isVisible = false
            }
        }

        popup.show()
    }

    @SuppressLint("RestrictedApi")
    override fun onValueChange(slider: Slider, value: Float, fromUser: Boolean) {
        PlayerService.setDeviceVolume(value.toInt())
    }

    @SuppressLint("RestrictedApi")
    override fun onStartTrackingTouch(slider: Slider) {
        PlayerService.setDeviceVolume(slider.value.toInt())
    }

    @SuppressLint("RestrictedApi")
    override fun onStopTrackingTouch(slider: Slider) {

    }

}