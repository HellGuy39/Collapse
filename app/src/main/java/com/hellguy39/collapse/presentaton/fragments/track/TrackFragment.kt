package com.hellguy39.collapse.presentaton.fragments.track

import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.TrackFragmentBinding


class TrackFragment : Fragment(R.layout.track_fragment), MediaPlayer.OnPreparedListener, View.OnClickListener {

    companion object {
        fun newInstance() = TrackFragment()
    }

    private val args: TrackFragmentArgs by navArgs()
    private lateinit var mediaPlayer: MediaPlayer

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
    }

    override fun onStart() {
        super.onStart()
        val uri = Uri.parse(args.track.path)
        mediaPlayer = MediaPlayer.create(requireContext(), uri)
        mediaPlayer.setOnPreparedListener(this)
        updateUI(uri)
    }

    override fun onPrepared(p0: MediaPlayer?) {
        mediaPlayer.start()
    }

    private fun updateUI(uri: Uri) {
        val mmr = MediaMetadataRetriever()
        mmr.setDataSource(context, uri)

        val cover = mmr.embeddedPicture
        val tittle = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
        val artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)

        _binding.tvTrackName.text = tittle
        _binding.tvPerformer.text = artist

        if (cover != null) {
            val bitmap = BitmapFactory.decodeByteArray(cover, 0, cover.size)
            _binding.ivCover.setImageBitmap(bitmap)
        } else {
            _binding.ivCover.setImageResource(R.drawable.ic_round_audiotrack_24)
        }

    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            _binding.ibPlayPause.id -> {

            }
            _binding.ibNextTrack.id -> {

            }
            _binding.ibPreviousTrack.id -> {

            }
            _binding.ibShuffle.id -> {

            }
            _binding.ibRepeatTrack.id -> {

            }
        }
    }
}