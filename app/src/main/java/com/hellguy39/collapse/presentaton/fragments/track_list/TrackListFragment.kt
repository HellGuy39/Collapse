package com.hellguy39.collapse.presentaton.fragments.track_list

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.doOnPreDraw
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class TrackListFragment : Fragment(R.layout.track_list_fragment), TracksAdapter.OnTrackListener,
    SearchView.OnQueryTextListener, View.OnClickListener {

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

    private var recyclerTracks = mutableListOf<Track>()

    private lateinit var adapter: TracksAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataViewModel = ViewModelProvider(activity as MainActivity)[MediaLibraryDataViewModel::class.java]

        receivedPlaylist = args.playlist

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        binding = TrackListFragmentBinding.bind(view)

        setupRecyclerView()

        binding.collapseToolbar.title = receivedPlaylist.name

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.tvPlaylistName.text = receivedPlaylist.name

        binding.btnPlay.setOnClickListener(this)
        binding.btnShuffle.setOnClickListener(this)

        searchView = binding.toolbar.menu.findItem(R.id.search).actionView as SearchView
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
        PlaylistType.Artist -> {
            binding.toolbar.menu.findItem(R.id.edit).isVisible = false
            binding.toolbar.menu.findItem(R.id.delete).isVisible = false
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
        PlaylistType.Artist -> {
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

        when (receivedPlaylist.type) {
            PlaylistType.AllTracks -> {
                setAllTracksObserver()
            }
            PlaylistType.Custom -> {
                onTracksReceived(receivedPlaylist.tracks, PlaylistType.Custom, receivedPlaylist.name)
            }
            PlaylistType.Favourites -> {
                setFavouritesTracksObserver()
            }
            PlaylistType.Artist -> {
                onTracksReceived(receivedPlaylist.tracks, PlaylistType.Artist, receivedPlaylist.name)
            }
        }
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
            onTracksReceived(receivedTracks, PlaylistType.AllTracks, "All tracks")
        }
    }

    private fun setFavouritesTracksObserver() {
        dataViewModel.getAllFavouriteTracks().observe(viewLifecycleOwner) { receivedTracks ->
            onTracksReceived(receivedTracks, PlaylistType.Favourites, "Favourites")
        }
    }

    private fun setContentObserver() {
        PlayerService.getContentPosition().observe(viewLifecycleOwner) {
            val serviceType = PlayerService.getServiceContent().playlist?.type

            if (receivedPlaylist.type == serviceType) {
                when(serviceType) {
                    PlaylistType.Custom -> {
                        if (receivedPlaylist.id == PlayerService.getServiceContent().playlist?.id) {
                            updateListPlayingPosition(position = it)
                        }
                    }
                    PlaylistType.Favourites -> {
                        updateListPlayingPosition(position = it)
                    }
                    PlaylistType.AllTracks -> {
                        updateListPlayingPosition(position = it)
                    }
                    PlaylistType.Artist -> {
                        if (PlayerService.getServiceContent().playlist?.name == receivedPlaylist.name)
                            updateListPlayingPosition(position = it)
                    }
                }
            }
        }
    }

    private fun updateListPlayingPosition(position: Int) {
        adapter.updatePlayingItem(position)
    }

    private fun onTracksReceived(receivedTracks: List<Track>, playlistType: PlaylistType, name: String) {
        if (playlistType == PlaylistType.AllTracks || playlistType == PlaylistType.Favourites) {
            receivedPlaylist = Playlist(
                id = if(playlistType == PlaylistType.AllTracks) -1 else if (playlistType == PlaylistType.Favourites) -2 else null,
                name = name,
                type = playlistType,
                tracks = receivedTracks.toMutableList()
            )
        }
        clearRecyclerView()
        updateRecyclerView(receivedTracks)
    }

    private fun setupRecyclerView() {
        binding.rvTrackList.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        adapter = TracksAdapter(
            trackList = recyclerTracks,
            resources = resources,
            listener = this,
            getImageBitmapUseCase = getImageBitmapUseCase,
            context = requireContext(),
            favouriteTracksUseCases = favouriteTracksUseCases,
            playlistType = receivedPlaylist.type,
        )
        binding.rvTrackList.adapter = adapter
        binding.rvTrackList.doOnPreDraw {
            startPostponedEnterTransition()
        }
    }

    override fun onTrackClick(track: Track, position: Int) {
        startPlayer(position = position)
    }

    override fun onAddToFavourites(track: Track) {
        dataViewModel.addToFavourites(track = track)
    }

    override fun onDeleteFromPlaylist(track: Track, position: Int) {

        receivedPlaylist.tracks.removeAt(position)
        recyclerTracks.removeAt(position)

        binding.rvTrackList.adapter?.notifyItemRemoved(position)
        binding.rvTrackList.adapter?.notifyItemRangeChanged(0, recyclerTracks.size)

        when (receivedPlaylist.type) {
            PlaylistType.Favourites -> {
                dataViewModel.deleteFromFavourites(track)
            }
            else -> {
                dataViewModel.deleteFromPlaylist(playlist = receivedPlaylist)
            }
        }
    }

    private fun updateRecyclerView(receivedTracks: List<Track>) = CoroutineScope(Dispatchers.IO).launch {
        for (n in receivedTracks.indices) {
            recyclerTracks.add(receivedTracks[n])
        }
        withContext(Dispatchers.Main) {
            val position = binding.rvTrackList.adapter?.itemCount ?: 0
            binding.rvTrackList.adapter?.notifyItemRangeInserted(position, recyclerTracks.size)
            setContentObserver()
        }
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
            PlaylistType.Artist -> {
                queryList = dataViewModel.searchWithQueryInTrackList(
                    query = query?: "",
                    receivedPlaylist.tracks
                )
            }
        }

        clearRecyclerView()
        updateRecyclerView(queryList)
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            binding.btnPlay.id -> {
                startPlayer(0)
            }
            binding.btnShuffle.id -> {
                startPlayer(0, true)
            }
        }
    }

    private fun startPlayer(position: Int, startWithShuffle: Boolean = false) {
        PlayerService.startService(requireContext(), ServiceContentWrapper(
            type = PlayerType.LocalTrack,
            position = position,
            playlist = receivedPlaylist,
            startWithShuffle = startWithShuffle
        ))
    }
}