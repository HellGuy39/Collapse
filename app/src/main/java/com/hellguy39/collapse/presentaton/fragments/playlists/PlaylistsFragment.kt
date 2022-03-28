package com.hellguy39.collapse.presentaton.fragments.playlists

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.PlaylistsFragmentBinding

class PlaylistsFragment : Fragment(R.layout.playlists_fragment) {

    companion object {
        fun newInstance() = PlaylistsFragment()
    }

    private lateinit var viewModel: PlaylistsViewModel
    private lateinit var binding: PlaylistsFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[PlaylistsViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = PlaylistsFragmentBinding.bind(view)
        binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }
}