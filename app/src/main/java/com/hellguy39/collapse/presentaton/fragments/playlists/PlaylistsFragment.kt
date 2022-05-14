package com.hellguy39.collapse.presentaton.fragments.playlists

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.PlaylistsFragmentBinding
import com.hellguy39.collapse.presentaton.activities.main.MainActivity
import com.hellguy39.collapse.presentaton.adapters.PlaylistsAdapter
import com.hellguy39.collapse.presentaton.view_models.MediaLibraryDataViewModel
import com.hellguy39.collapse.utils.Action
import com.hellguy39.collapse.utils.getGridLayoutManager
import com.hellguy39.collapse.utils.setMaterialFadeThoughtAnimation
import com.hellguy39.collapse.utils.setOnBackFragmentNavigation
import com.hellguy39.domain.models.Playlist
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaylistsFragment : Fragment(R.layout.playlists_fragment),
    PlaylistsAdapter.OnPlaylistListener, SearchView.OnQueryTextListener {

    companion object {
        fun newInstance() = PlaylistsFragment()
    }

    private lateinit var dataViewModel: MediaLibraryDataViewModel

    private lateinit var binding: PlaylistsFragmentBinding

    private lateinit var searchView: SearchView

    private var playlists = mutableListOf<Playlist>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setMaterialFadeThoughtAnimation()

        dataViewModel = ViewModelProvider(activity as MainActivity)[MediaLibraryDataViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()

        binding = PlaylistsFragmentBinding.bind(view)
        binding.topAppBar.setOnBackFragmentNavigation()

        val searchItem = binding.topAppBar.menu.findItem(R.id.search)
        searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(this)

        setupRecyclerView()

        binding.fabAdd.setOnClickListener {
            findNavController().navigate(
                PlaylistsFragmentDirections.actionPlaylistsFragmentToCreatePlaylistFragment(
                    Playlist(),
                    Action.Create
                ), FragmentNavigatorExtras(binding.fabAdd to "create_playlist_transition")
            )
        }

        setObservers()
    }

    private fun setupRecyclerView() = binding.rvPlaylists.apply {
        layoutManager = getGridLayoutManager(context)
        adapter = PlaylistsAdapter(
            playlists = playlists,
            listener = this@PlaylistsFragment,
        )
        doOnPreDraw {
            startPostponedEnterTransition()
        }
    }

    private  fun setObservers() {
        dataViewModel.getAllPlaylists().observe(viewLifecycleOwner) {
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

    override fun onPlaylistClick(playlist: Playlist, view: View) {
        view.transitionName = "playlist_transition"
        findNavController().navigate(
            PlaylistsFragmentDirections.actionPlaylistsFragmentToTrackListFragment(
                playlist
            ),FragmentNavigatorExtras(view to "playlist_transition")
        )
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        onSearchViewChangeQuery(query = query)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        onSearchViewChangeQuery(query = newText)
        return false
    }

    private fun onSearchViewChangeQuery(query: String?) {
        val queryList = dataViewModel.searchWithQueryInPlaylists(
            query = query?: "",
            dataViewModel.getAllPlaylists().value
        )

        clearRecyclerView()
        updateRecyclerView(queryList)
    }

}