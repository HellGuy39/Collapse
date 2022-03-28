package com.hellguy39.collapse.presentaton.fragments.create_playlist

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.CreatePlaylistFragmentBinding

class CreatePlaylistFragment : Fragment(R.layout.create_playlist_fragment) {

    companion object {
        fun newInstance() = CreatePlaylistFragment()
    }

    private lateinit var viewModel: CreatePlaylistViewModel
    private lateinit var binding: CreatePlaylistFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[CreatePlaylistViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CreatePlaylistFragmentBinding.bind(view)
    }
}