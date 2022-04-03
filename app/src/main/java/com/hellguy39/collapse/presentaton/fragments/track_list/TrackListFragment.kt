package com.hellguy39.collapse.presentaton.fragments.track_list

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.TrackListFragmentBinding
import com.hellguy39.collapse.presentaton.activities.main.MainActivity
import com.hellguy39.collapse.presentaton.adapters.TracksAdapter
import com.hellguy39.collapse.presentaton.services.PlayerService
import com.hellguy39.collapse.presentaton.view_models.MediaLibraryDataViewModel
import com.hellguy39.collapse.utils.Action
import com.hellguy39.domain.models.Playlist
import com.hellguy39.domain.models.ServiceContentWrapper
import com.hellguy39.domain.models.Track
import com.hellguy39.domain.usecases.ConvertByteArrayToBitmapUseCase
import com.hellguy39.domain.usecases.GetImageBitmapUseCase
import com.hellguy39.domain.usecases.favourites.FavouriteTracksUseCases
import com.hellguy39.domain.utils.PlayerType
import com.hellguy39.domain.utils.PlaylistType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TrackListFragment : Fragment(R.layout.track_list_fragment), TracksAdapter.OnTrackListener,
    SearchView.OnQueryTextListener {

    companion object {
        fun newInstance() = TrackListFragment()
    }

    @Inject
    lateinit var convertByteArrayToBitmapUseCase: ConvertByteArrayToBitmapUseCase

    @Inject
    lateinit var getImageBitmapUseCase: GetImageBitmapUseCase

    @Inject
    lateinit var favouriteTracksUseCases: FavouriteTracksUseCases

    private lateinit var dataViewModel: MediaLibraryDataViewModel
    private lateinit var binding: TrackListFragmentBinding
    private val args: TrackListFragmentArgs by navArgs()

    private lateinit var searchView: SearchView

    private lateinit var receivedPlaylist: Playlist

    private var allTracksFromCurrentPlaylist = listOf<Track>()
    private var recyclerTracks = mutableListOf<Track>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataViewModel = ViewModelProvider(activity as MainActivity)[MediaLibraryDataViewModel::class.java]

        receivedPlaylist = args.playlist

        allTracksFromCurrentPlaylist = receivedPlaylist.tracks
        recyclerTracks = receivedPlaylist.tracks.toMutableList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = TrackListFragmentBinding.bind(view)

        setupRecyclerView()

        binding.collapseToolbar.title = receivedPlaylist.name

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        val searchItem = binding.toolbar.menu.findItem(R.id.search)
        searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(this)

        setupToolbarImage(receivedPlaylist.type)
        setupToolbarMenu(receivedPlaylist.type)

    }

    private fun setupToolbarMenu(type: Enum<PlaylistType>) = when(type) {
        PlaylistType.AllTracks -> {
            binding.toolbar.menu.findItem(R.id.edit).isVisible = false
            binding.toolbar.menu.findItem(R.id.delete).isVisible = false
        }
        PlaylistType.Favourites -> {
            binding.toolbar.menu.findItem(R.id.edit).isVisible = false
            binding.toolbar.menu.findItem(R.id.delete).isVisible = false
        }
        PlaylistType.Custom -> {
            binding.toolbar.setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.edit -> {
                        navigateToEditPlaylist()
                        true
                    }
                    R.id.delete -> {
                        showDeleteDialog()
                        true
                    }
                    else -> false
                }
            }
        }
        else -> {}
    }

    private fun setupToolbarImage(type: Enum<PlaylistType>) = when(type) {
        PlaylistType.AllTracks -> {
            binding.toolbarImage.visibility = View.GONE
        }
        PlaylistType.Favourites -> {
            binding.toolbarImage.visibility = View.GONE
        }
        PlaylistType.Custom -> {
            val bytes = receivedPlaylist.picture
            if (bytes != null)
                binding.toolbarImage.setImageBitmap(convertByteArrayToBitmapUseCase.invoke(bytes))
            else
                binding.toolbarImage.visibility = View.GONE
        }
        else -> {}
    }


    private fun navigateToEditPlaylist() = findNavController().navigate(
        TrackListFragmentDirections.actionTrackListFragmentToCreatePlaylistFragment(
            receivedPlaylist,
            Action.Update
        )
    )

    override fun onResume() {
        super.onResume()
//        CoroutineScope(Dispatchers.Main).launch {
//            delay(400)
            when (receivedPlaylist.type) {
                PlaylistType.AllTracks -> {
                    setAllTracksObserver()
                }
                PlaylistType.Custom -> {
                    clearRecyclerView()
                    updateRecyclerView(receivedPlaylist.tracks)
                }
                PlaylistType.Favourites -> {
                    setFavouritesTracksObserver()
                }
            }
        //}
    }

    private fun setupRecyclerView() {
        binding.rvTrackList.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.rvTrackList.adapter = TracksAdapter(
            trackList = recyclerTracks,
            resources = resources,
            listener = this,
            getImageBitmapUseCase = getImageBitmapUseCase,
            context = requireContext(),
            favouriteTracksUseCases = favouriteTracksUseCases,
            playlistType = receivedPlaylist.type
        )
    }

    private fun showDeleteDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Are you sure want to delete this playlist?")
            .setNeutralButton("Cancel") { dialog, which ->
                dialog.cancel()
            }
            .setPositiveButton("Yes") { dialog, which ->
                dataViewModel.deletePlaylist(playlist = receivedPlaylist)
                findNavController().popBackStack()

            }
            .show()
    }

    private fun setAllTracksObserver() {
        dataViewModel.getAllTracks().observe(viewLifecycleOwner) { receivedTracks ->
            allTracksFromCurrentPlaylist = receivedTracks
            receivedPlaylist = Playlist(
                name = "All tracks",
                type = PlaylistType.AllTracks,
                tracks = receivedTracks
            )
            clearRecyclerView()
            updateRecyclerView(receivedTracks)
        }
    }

    private fun setFavouritesTracksObserver() {
        dataViewModel.getAllFavouriteTracks().observe(viewLifecycleOwner) { receivedTracks ->
            allTracksFromCurrentPlaylist = receivedTracks
            receivedPlaylist = Playlist(
                name = "Favourites",
                type = PlaylistType.Favourites,
                tracks = receivedTracks
            )
            clearRecyclerView()
            updateRecyclerView(receivedTracks)
        }
    }

    override fun onTrackClick(track: Track) {
        PlayerService.startService(requireContext(), ServiceContentWrapper(
                type = PlayerType.LocalTrack,
                position = allTracksFromCurrentPlaylist.indexOf(track),
                playlist = receivedPlaylist,
            )
        )
    }

    override fun onAddToFavourites(track: Track) {
        dataViewModel.addToFavourites(track = track)
    }

    override fun onDeleteFromPlaylist(track: Track, position: Int) {
        dataViewModel.deleteFromPlaylist(track = track, playlist = receivedPlaylist)

        val updatedList = mutableListOf<Track>()
        updatedList.addAll(receivedPlaylist.tracks)
        updatedList.removeAt(position)
        receivedPlaylist.tracks = updatedList
        recyclerTracks.removeAt(position)

        binding.rvTrackList.adapter?.notifyItemRemoved(position)
    }

    private fun updateRecyclerView(receivedTracks: List<Track>) {
        for (n in receivedTracks.indices) {
            recyclerTracks.add(receivedTracks[n])
        }
        val position = binding.rvTrackList.adapter?.itemCount ?: 0
        binding.rvTrackList.adapter?.notifyItemRangeInserted(position, recyclerTracks.size)
    }

    private fun clearRecyclerView() {
        val size = binding.rvTrackList.adapter?.itemCount
        recyclerTracks.clear()
        binding.rvTrackList.adapter?.notifyItemRangeRemoved(0, size ?: 0)
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
        var queryList = listOf<Track>()

        when (receivedPlaylist.type) {
            PlaylistType.AllTracks -> {
                queryList = dataViewModel.searchWithQueryInTrackList(
                    query = query?: "",
                    dataViewModel.getAllTracks().value
                )
            }
            PlaylistType.Custom -> {
                queryList = dataViewModel.searchWithQueryInTrackList(
                    query = query?: "",
                    receivedPlaylist.tracks
                )
            }
            PlaylistType.Favourites -> {
                queryList = dataViewModel.searchWithQueryInTrackList(
                    query = query?: "",
                    dataViewModel.getAllFavouriteTracks().value
                )
            }
        }

        clearRecyclerView()
        updateRecyclerView(queryList)
    }

}