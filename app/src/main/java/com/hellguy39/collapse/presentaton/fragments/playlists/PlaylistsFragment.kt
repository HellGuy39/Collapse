package com.hellguy39.collapse.presentaton.fragments.playlists

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.PlaylistsFragmentBinding
import com.hellguy39.collapse.presentaton.activities.main.MainActivity
import com.hellguy39.collapse.presentaton.adapters.PlaylistsAdapter
import com.hellguy39.collapse.presentaton.view_models.MediaLibraryDataViewModel
import com.hellguy39.collapse.utils.Action
import com.hellguy39.domain.models.Playlist
import com.hellguy39.domain.models.Track
import com.hellguy39.domain.usecases.ConvertByteArrayToBitmapUseCase
import com.hellguy39.domain.utils.PlaylistType
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlaylistsFragment : Fragment(R.layout.playlists_fragment),
    PlaylistsAdapter.OnPlaylistListener, SearchView.OnQueryTextListener {

    @Inject
    lateinit var convertByteArrayToBitmapUseCase: ConvertByteArrayToBitmapUseCase

    companion object {
        fun newInstance() = PlaylistsFragment()
    }

    private lateinit var dataViewModel: MediaLibraryDataViewModel

    private lateinit var binding: PlaylistsFragmentBinding

    private lateinit var searchView: SearchView

    private var playlists = mutableListOf<Playlist>()
    private lateinit var adapter: PlaylistsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataViewModel = ViewModelProvider(activity as MainActivity)[MediaLibraryDataViewModel::class.java]
        adapter = PlaylistsAdapter(
            playlists = playlists,
            listener = this,
            convertByteArrayToBitmapUseCase = convertByteArrayToBitmapUseCase
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = PlaylistsFragmentBinding.bind(view)
        binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        val searchItem = binding.topAppBar.menu.findItem(R.id.search)
        searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(this)

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
                        PlaylistsFragmentDirections.actionPlaylistsFragmentToCreatePlaylistFragment(
                            Playlist(),
                            Action.Create
                        )
                    )
                    true
                }
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setObservers()
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

    override fun onPlaylistClick(playlist: Playlist) {
        findNavController().navigate(
            PlaylistsFragmentDirections.actionPlaylistsFragmentToTrackListFragment(
                playlist,
                null
            )
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