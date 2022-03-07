package com.hellguy39.collapse.presentaton.fragments.home

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.FragmentHomeBinding
import com.hellguy39.collapse.presentaton.adapters.RecentPlaylistsAdapter
import com.hellguy39.data.models.Playlist
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var _binding: FragmentHomeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        _binding.topAppBar.title = "Hello world"

        val list = mutableListOf<Playlist>()

        val playlist = Playlist(
            tittle = "Smells Like Teen Spirit"
        )

        list.add(playlist)
        list.add(playlist)

        _binding.rvRecentPlaylists.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = RecentPlaylistsAdapter(playlistList = list, resources = resources)
        }

    }

}