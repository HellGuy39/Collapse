package com.hellguy39.collapse.presentaton.fragments.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var _binding: FragmentHomeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        _binding.topAppBar.title = "Hello world"

        /*val list = mutableListOf<Playlist>()

        val playlist = Playlist(
            tittle = "Smells Like Teen Spirit"
        )

        list.add(playlist)
        list.add(playlist)

        _binding.rvRecentPlaylists.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = RecentPlaylistsAdapter(playlistList = list, resources = resources)
        }*/

    }

}