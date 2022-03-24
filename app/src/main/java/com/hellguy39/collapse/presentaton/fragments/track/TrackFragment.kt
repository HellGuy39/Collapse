package com.hellguy39.collapse.presentaton.fragments.track

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.material.transition.MaterialSharedAxis
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.TrackFragmentBinding
import com.hellguy39.collapse.presentaton.services.PlayerService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class TrackFragment : Fragment(R.layout.track_fragment), View.OnClickListener {

    companion object {
        fun newInstance() = TrackFragment()
    }

    private lateinit var _viewModel: TrackViewModel
    private lateinit var _binding: TrackFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewModel = ViewModelProvider(this)[TrackViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = TrackFragmentBinding.bind(view)
        _binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        _binding.ibPlayPause.setImageResource(R.drawable.ic_round_pause_24)

        _binding.ibPlayPause.setOnClickListener(this)
        _binding.ibNextTrack.setOnClickListener(this)
        _binding.ibPreviousTrack.setOnClickListener(this)
        _binding.ibShuffle.setOnClickListener(this)
        _binding.ibRepeatTrack.setOnClickListener(this)

        _binding.sliderTime.addOnChangeListener { slider, value, fromUser ->
            if (fromUser)
                PlayerService.onSeekTo((value * 1000).toLong())
        }

        setObservers()
    }

    private fun setObservers() {
        PlayerService.isPlaying().observe(viewLifecycleOwner) {
            if (it) {
                _binding.ibPlayPause.setImageResource(R.drawable.ic_round_pause_24)
            } else {
                _binding.ibPlayPause.setImageResource(R.drawable.ic_round_play_arrow_24)
            }
        }

        PlayerService.getCurrentMetadata().observe(viewLifecycleOwner) {
            if (it != null)
                updateUI(it)
        }
    }

    private fun updateUI(mediaMetadata: MediaMetadata) {

        CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                val duration = PlayerService.getDuration() / 1000
                val position = PlayerService.getCurrentPosition() / 1000

                if (duration > 0 || position > 0) {
                    _binding.sliderTime.apply {
                        valueFrom = 0f
                        valueTo = (duration).toFloat()
                        value = (position).toFloat()
                    }
                }
                delay(1000)
            }
        }

        _binding.sliderTime.setLabelFormatter {
            SimpleDateFormat("m:ss", Locale.getDefault()).format(Date(it.toLong() * 1000))
        }

        _binding.tvTrackName.text = mediaMetadata.title ?: "Unknown"
        _binding.tvPerformer.text = mediaMetadata.artist ?: "Unknown"

        val bytes = mediaMetadata.artworkData

        if (bytes != null)
            _binding.ivCover.setImageBitmap(BitmapFactory.decodeByteArray(mediaMetadata.artworkData, 0, bytes.size))
        else
            _binding.ivCover.setImageResource(R.drawable.ic_round_audiotrack_24)
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            _binding.ibPlayPause.id -> {
                if (PlayerService.isPlaying().value == true)
                    PlayerService.onPause()
                else
                    PlayerService.onPlay()
            }
            _binding.ibNextTrack.id -> {
                PlayerService.onNext()
            }
            _binding.ibPreviousTrack.id -> {
                PlayerService.onPrevious()
            }
            _binding.ibShuffle.id -> {
                if (PlayerService.isShuffle())
                    PlayerService.onShuffle(false)
                else
                    PlayerService.onShuffle(true)
            }
            _binding.ibRepeatTrack.id -> {
                if (PlayerService.isRepeat() == 0)
                    PlayerService.onRepeat(2)
                else
                    PlayerService.onRepeat(0)
            }
        }
    }
}