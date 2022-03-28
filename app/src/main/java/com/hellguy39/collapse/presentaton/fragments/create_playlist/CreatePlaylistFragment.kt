package com.hellguy39.collapse.presentaton.fragments.create_playlist

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.exoplayer2.extractor.mp4.Track
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.CreatePlaylistFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreatePlaylistFragment : Fragment(R.layout.create_playlist_fragment), View.OnClickListener {

    companion object {
        fun newInstance() = CreatePlaylistFragment()
    }

    private lateinit var viewModel: CreatePlaylistViewModel
    private lateinit var binding: CreatePlaylistFragmentBinding

    private var tracks = mutableListOf<Track>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[CreatePlaylistViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CreatePlaylistFragmentBinding.bind(view)

        binding.topAppBar.setNavigationOnClickListener { findNavController().popBackStack() }
        binding.fabAdd.setOnClickListener(this)
        binding.btnSelectTracks.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            R.id.fabAdd -> {

            }
            R.id.btnSelectTracks -> {
                findNavController().navigate(
                    CreatePlaylistFragmentDirections.actionCreatePlaylistFragmentToSelectTracksFragment()
                )
            }
        }
    }
}