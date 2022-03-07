package com.hellguy39.collapse.presentaton.fragments.playlist

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.PlaylistFragmentBinding

class PlaylistFragment : Fragment(R.layout.playlist_fragment) {

    companion object {
        fun newInstance() = PlaylistFragment()
    }

    private lateinit var _viewModel: PlaylistViewModel
    private lateinit var _binding: PlaylistFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewModel = ViewModelProvider(this)[PlaylistViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = PlaylistFragmentBinding.bind(view)
    }

}