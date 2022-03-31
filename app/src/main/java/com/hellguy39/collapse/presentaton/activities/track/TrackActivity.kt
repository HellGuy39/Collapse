package com.hellguy39.collapse.presentaton.activities.track

import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.android.exoplayer2.MediaMetadata
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.ActivityTrackBinding
import com.hellguy39.collapse.presentaton.services.PlayerService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class TrackActivity : AppCompatActivity(), View.OnClickListener {

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

        binding.topAppBar.title = PlayerService.getPlaylistName()

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
        }
    }

}