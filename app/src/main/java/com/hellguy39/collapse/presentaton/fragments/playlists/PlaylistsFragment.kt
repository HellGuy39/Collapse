package com.hellguy39.collapse.presentaton.fragments.playlists

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.PlaylistsFragmentBinding
import com.hellguy39.collapse.presentaton.adapters.PlaylistsAdapter
import com.hellguy39.domain.models.Playlist
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaylistsFragment : Fragment(R.layout.playlists_fragment),
    PlaylistsAdapter.OnPlaylistListener {

    companion object {
        fun newInstance() = PlaylistsFragment()
    }

    private lateinit var viewModel: PlaylistsViewModel
    private lateinit var binding: PlaylistsFragmentBinding

    private var playlists = mutableListOf<Playlist>()
    private lateinit var adapter: PlaylistsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[PlaylistsViewModel::class.java]
        adapter = PlaylistsAdapter(playlists = playlists, listener = this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = PlaylistsFragmentBinding.bind(view)
        binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.rvPlaylists.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.rvPlaylists.adapter = adapter
        binding.topAppBar.setOnMenuItemClickListener {
            when(it?.itemId) {
                R.id.add -> {
                    findNavController().navigate(
                        PlaylistsFragmentDirections.actionPlaylistsFragmentToCreatePlaylistFragment()
                    )
                    true
                }
                else -> false
            }
        }
        setObservers()
    }

    private  fun setObservers() {
        viewModel.getPlaylists().observe(viewLifecycleOwner) {
            clearRecyclerView()
            updateRecyclerView(receivedList = it)
        }
    }

    private fun updateRecyclerView(receivedList: List<Playlist>) {

        for (n in receivedList.indices) {
            playlists.add(receivedList[n])
        }

        val position = binding.rvPlaylists.adapter?.itemCount ?: 0
        binding.rvPlaylists.adapter?.notifyItemRangeInserted(position, playlists.size)
    }

    private fun clearRecyclerView() {
        val size = binding.rvPlaylists.adapter?.itemCount
        playlists.clear()
        binding.rvPlaylists.adapter?.notifyItemRangeRemoved(0, size ?: 0)
    }

    override fun onPlaylistClick(playlist: Playlist) {
        findNavController().navigate(
            PlaylistsFragmentDirections.actionPlaylistsFragmentToTrackListFragment(playlist)
        )
    }
}