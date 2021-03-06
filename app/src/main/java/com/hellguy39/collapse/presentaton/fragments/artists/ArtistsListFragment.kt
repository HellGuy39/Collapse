package com.hellguy39.collapse.presentaton.fragments.artists

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.FragmentArtistsListBinding
import com.hellguy39.collapse.presentaton.activities.main.MainActivity
import com.hellguy39.collapse.presentaton.adapters.ArtistsAdapter
import com.hellguy39.collapse.presentaton.view_models.MediaLibraryDataViewModel
import com.hellguy39.collapse.utils.getArtistItemVerticalDivider
import com.hellguy39.collapse.utils.getVerticalLayoutManager
import com.hellguy39.collapse.utils.setMaterialFadeThoughtAnimation
import com.hellguy39.collapse.utils.setOnBackFragmentNavigation
import com.hellguy39.domain.models.Playlist
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArtistsListFragment : Fragment(R.layout.fragment_artists_list),
    ArtistsAdapter.OnArtistListener, SearchView.OnQueryTextListener {

    private lateinit var binding: FragmentArtistsListBinding
    private lateinit var dataViewModel: MediaLibraryDataViewModel

    private var playlists = mutableListOf<Playlist>()

    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setMaterialFadeThoughtAnimation()
        dataViewModel = ViewModelProvider(activity as MainActivity)[MediaLibraryDataViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentArtistsListBinding.bind(view)

        postponeEnterTransition()

        setupRecyclerView()

        searchView = binding.topAppBar.menu.findItem(R.id.search).actionView as SearchView
        searchView.setOnQueryTextListener(this)

        binding.topAppBar.setOnBackFragmentNavigation()
        setArtistsObserver()
    }

    private fun setArtistsObserver() {
        dataViewModel.getAllArtists().observe(viewLifecycleOwner) { receivedArtists ->
            clearRecyclerView()
            updateRecyclerView(receivedArtists)
        }
    }

    private fun navigateToTrackList(playlist: Playlist) = findNavController().navigate(
        ArtistsListFragmentDirections.actionArtistsListFragmentToTrackListFragment(playlist)
    )

    private fun setupRecyclerView() = binding.rvArtists.apply {
        layoutManager = getVerticalLayoutManager(requireContext())
        adapter = ArtistsAdapter(
            playlists = playlists,
            listener = this@ArtistsListFragment
        )
        doOnPreDraw {
            startPostponedEnterTransition()
        }
        addItemDecoration(this.getArtistItemVerticalDivider(requireContext()))
    }

    private fun updateRecyclerView(receivedArtists: List<Playlist>) {
        for (n in receivedArtists.indices) {
            playlists.add(receivedArtists[n])
        }
        val position = binding.rvArtists.adapter?.itemCount ?: 0
        binding.rvArtists.adapter?.notifyItemRangeInserted(position, playlists.size)
        checkEmptyView()
    }

    private fun checkEmptyView() {
        binding.emptyView.root.isVisible = binding.rvArtists.adapter?.itemCount == 0
    }

    private fun clearRecyclerView() {
        val size = binding.rvArtists.adapter?.itemCount
        playlists.clear()
        binding.rvArtists.adapter?.notifyItemRangeRemoved(0, size ?: 0)
    }

    override fun onArtistClick(position: Int) {
        navigateToTrackList(playlist = playlists[position])
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        onSearchViewChangeQuery(query)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        onSearchViewChangeQuery(newText)
        return false
    }

    private fun onSearchViewChangeQuery(query: String?) {
        val queryList = dataViewModel.searchWithQueryInArtists(
            query = query ?: "",
            dataViewModel.getAllArtists().value
        )
        clearRecyclerView()
        updateRecyclerView(queryList)
    }
}