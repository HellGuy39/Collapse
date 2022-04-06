package com.hellguy39.collapse.presentaton.fragments.artists

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.FragmentArtistsListBinding
import com.hellguy39.collapse.presentaton.activities.main.MainActivity
import com.hellguy39.collapse.presentaton.adapters.ArtistsAdapter
import com.hellguy39.collapse.presentaton.view_models.MediaLibraryDataViewModel
import com.hellguy39.domain.models.Artist
import com.hellguy39.domain.models.Playlist
import com.hellguy39.domain.utils.PlaylistType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArtistsListFragment : Fragment(R.layout.fragment_artists_list),
    ArtistsAdapter.OnArtistListener, SearchView.OnQueryTextListener {

    private lateinit var binding: FragmentArtistsListBinding
    private lateinit var dataViewModel: MediaLibraryDataViewModel

    private var artists = mutableListOf<Artist>()

    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataViewModel = ViewModelProvider(activity as MainActivity)[MediaLibraryDataViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentArtistsListBinding.bind(view)

        setupRecyclerView()

        searchView = binding.topAppBar.menu.findItem(R.id.search).actionView as SearchView
        searchView.setOnQueryTextListener(this)

        binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onResume() {
        super.onResume()
        setArtistsObserver()
    }

    private fun setArtistsObserver() {
        dataViewModel.getAllArtists().observe(viewLifecycleOwner) { receivedArtists ->
            clearRecyclerView()
            updateRecyclerView(receivedArtists)
        }
    }

    private fun navigateToTrackList(artist: Artist) = findNavController().navigate(
        ArtistsListFragmentDirections.actionArtistsListFragmentToTrackListFragment(
            Playlist(
                name = artist.name,
                type = PlaylistType.Artist,
                tracks = artist.trackList
            ), artist
        )
    )

    private fun setupRecyclerView() {
        binding.rvArtists.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.rvArtists.adapter = ArtistsAdapter(
            artists = artists,
            listener = this
        )
    }

    private fun updateRecyclerView(receivedArtists: List<Artist>) {
        for (n in receivedArtists.indices) {
            artists.add(receivedArtists[n])
        }
        val position = binding.rvArtists.adapter?.itemCount ?: 0
        binding.rvArtists.adapter?.notifyItemRangeInserted(position, artists.size)
    }

    private fun clearRecyclerView() {
        val size = binding.rvArtists.adapter?.itemCount
        artists.clear()
        binding.rvArtists.adapter?.notifyItemRangeRemoved(0, size ?: 0)
    }

    override fun onArtistClick(position: Int) {
        navigateToTrackList(artist = artists[position])
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