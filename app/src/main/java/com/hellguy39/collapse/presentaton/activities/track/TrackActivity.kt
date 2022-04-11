package com.hellguy39.collapse.presentaton.activities.track

import android.R.attr.bitmap
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.MenuRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.palette.graphics.Palette
import androidx.palette.graphics.Palette.PaletteAsyncListener
import com.google.android.exoplayer2.MediaMetadata
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.ActivityTrackBinding
import com.hellguy39.collapse.presentaton.adapters.TracksAdapter
import com.hellguy39.collapse.presentaton.services.PlayerService
import com.hellguy39.domain.models.Track
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
class TrackActivity : AppCompatActivity(), View.OnClickListener, TracksAdapter.OnTrackListener {

    @Inject
    lateinit var playlistUseCases: PlaylistUseCases

    @Inject
    lateinit var favouriteTracksUseCase: FavouriteTracksUseCases

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

        if (bytes != null)
            binding.ivCover.setImageBitmap(BitmapFactory.decodeByteArray(mediaMetadata.artworkData, 0, bytes.size))
        else
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
                if (PlayerService.isRepeat() == 0)
                    PlayerService.onRepeat(2)
                else
                    PlayerService.onRepeat(0)
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
//
//    private fun updatePalette(bitmap: Bitmap) {
//        Palette.from(bitmap).generate({ palette ->
//            val dominant colorpalette.getDominantColor()
//        })
//    }

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

}