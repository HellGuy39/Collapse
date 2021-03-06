package com.hellguy39.collapse.presentaton.fragments.track_list

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.TrackListFragmentBinding
import com.hellguy39.collapse.presentaton.activities.main.MainActivity
import com.hellguy39.collapse.presentaton.adapters.TracksAdapter
import com.hellguy39.collapse.presentaton.fragments.trackMenuBottomSheet.PlaylistMenuBottomSheet
import com.hellguy39.collapse.presentaton.fragments.trackMenuBottomSheet.TrackMenuBottomSheet
import com.hellguy39.collapse.presentaton.services.PlayerService
import com.hellguy39.collapse.presentaton.view_models.MediaLibraryDataViewModel
import com.hellguy39.collapse.utils.*
import com.hellguy39.domain.models.Playlist
import com.hellguy39.domain.models.ServiceContentWrapper
import com.hellguy39.domain.models.Track
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
class TrackListFragment : Fragment(R.layout.track_list_fragment),
    TracksAdapter.OnTrackListener,
    SearchView.OnQueryTextListener,
    View.OnClickListener,
    TrackMenuEvents,
    PlaylistMenuEvents {

    companion object {
        fun newInstance() = TrackListFragment()
    }

    @Inject
    lateinit var favouriteTracksUseCases: FavouriteTracksUseCases

    private lateinit var dataViewModel: MediaLibraryDataViewModel
    private lateinit var binding: TrackListFragmentBinding
    private val args: TrackListFragmentArgs by navArgs()

    private lateinit var searchView: SearchView

    private lateinit var receivedPlaylist: Playlist

    private var recyclerTracks = mutableListOf<Track>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataViewModel = ViewModelProvider(activity as MainActivity)[MediaLibraryDataViewModel::class.java]

        setMaterialFadeThoughtAnimation()

        receivedPlaylist = args.playlist
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()

        binding = TrackListFragmentBinding.bind(view)

        setupRecyclerView()

        binding.toolbar.setOnBackFragmentNavigation()

        binding.btnPlay.setOnClickListener(this)
        binding.btnShuffle.setOnClickListener(this)

        searchView = binding.toolbar.menu.findItem(R.id.search).actionView as SearchView
        searchView.setOnQueryTextListener(this)

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

    private fun setupToolbarImage(type: Enum<PlaylistType>) = when(type) {
        PlaylistType.AllTracks -> {
            binding.toolbarImage.visibility = View.GONE
        }
        PlaylistType.Favourites -> {
            binding.toolbarImage.visibility = View.GONE
        }
        PlaylistType.Custom -> {
            val bytes = receivedPlaylist.picture
            if (bytes != null) {
                binding.toolbarImage.setImageBitmap(bytes.toBitmap())
            } else
                binding.toolbarImage.visibility = View.GONE
        }
        PlaylistType.Artist -> {
            val bytes = receivedPlaylist.picture
            if (bytes != null)
                binding.toolbarImage.setImageBitmap(bytes.toBitmap())
            else
                binding.toolbarImage.visibility = View.GONE
        }
        else -> {}
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
        (binding.rvTrackList.adapter as TracksAdapter).updatePlayingItem(position)
    }

    private fun onTracksReceived(receivedTracks: List<Track>, playlistType: PlaylistType, name: String) {

        binding.collapseToolbar.title = receivedPlaylist.name
        binding.tvPlaylistName.text = receivedPlaylist.name

        setupToolbarImage(receivedPlaylist.type)
        binding.toolbar.menu.findItem(R.id.more).setOnMenuItemClickListener {
            val bottomSheet = PlaylistMenuBottomSheet(
                playlist = receivedPlaylist,
                listener = this
            )
            bottomSheet.show(childFragmentManager, PlaylistMenuBottomSheet.PLAYLIST_MENU_TAG)
            true
        }

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

    private fun setupRecyclerView() = binding.rvTrackList.apply {
        addItemDecoration(this.getTrackItemVerticalDivider(requireContext()))
        layoutManager = getVerticalLayoutManager(requireContext())
        doOnPreDraw {
            startPostponedEnterTransition()
        }
        adapter = TracksAdapter(
            trackList = recyclerTracks,
            listener = this@TrackListFragment,
            context = requireContext(),
            playlistType = receivedPlaylist.type,
        )
        //setHasFixedSize(true)
        //setItemViewCacheSize(20)
    }

    override fun onTrackMenuClick(track: Track, position: Int, playlistType: Enum<PlaylistType>) {
        val bottomSheet = TrackMenuBottomSheet(track = track,
            position = position,
            listener = this,
            playlistType = playlistType,
            isTrackFavouriteUseCase = favouriteTracksUseCases.isTrackFavouriteUseCase,
        )
        bottomSheet.show(childFragmentManager, TrackMenuBottomSheet.TRACK_MENU_TAG)
    }

    override fun onTrackClick(track: Track, position: Int) {
        startPlayer(position = position, skipPauseClick = false)
    }

    override fun onAddToFavourites(track: Track) {
        dataViewModel.addToFavourites(track = track)
    }

    override fun onDeleteFromFavourites(track: Track) {
        dataViewModel.deleteFromFavouritesWithoutId(track)
    }

    override fun onDeleteFromPlaylist(track: Track, position: Int) {

        if (receivedPlaylist.tracks.size == 1) {
            receivedPlaylist.tracks = mutableListOf()
        } else {
            receivedPlaylist.tracks.removeAt(position)
        }

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

    private fun checkEmptyView() {
        binding.emptyView.root.isVisible = binding.rvTrackList.adapter?.itemCount == 0
    }

    private fun updateRecyclerView(receivedTracks: List<Track>) = CoroutineScope(Dispatchers.IO).launch {
        for (n in receivedTracks.indices) {
            recyclerTracks.add(receivedTracks[n])
        }
        withContext(Dispatchers.Main) {
            val position = binding.rvTrackList.adapter?.itemCount ?: 0
            binding.rvTrackList.adapter?.notifyItemRangeInserted(position, recyclerTracks.size)
            setContentObserver()
            checkEmptyView()
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
                startPlayer(position = 0, skipPauseClick = true)
            }
            binding.btnShuffle.id -> {
                startPlayer(position = 0, startWithShuffle = true, skipPauseClick = true)
            }
        }
    }

    private fun startPlayer(position: Int, startWithShuffle: Boolean = false, skipPauseClick: Boolean) {
        if (receivedPlaylist.tracks.size == 0) {
            Toast.makeText(requireContext(), "Nothing to play", Toast.LENGTH_SHORT).show()
            return
        }

        PlayerService.startService(requireContext(), ServiceContentWrapper(
            type = PlayerType.LocalTrack,
            position = position,
            playlist = receivedPlaylist,
            startWithShuffle = startWithShuffle,
            skipPauseClick = skipPauseClick
        ))
    }

    override fun onEditPlaylist() {
        setFragmentResultListener("updatedPlaylist") { requestKey, bundle ->
            receivedPlaylist = bundle.get("playlist") as Playlist
            onTracksReceived(receivedPlaylist.tracks, PlaylistType.Custom, receivedPlaylist.name)
        }

        navigateToEditPlaylist()
    }

    override fun onDeletePlaylist() {
        showDialog(
            title = "Confirm deletion",
            message = "Are you sure want to delete this playlist?",
            positiveButtonText = "Confirm",
            iconId = R.drawable.ic_round_delete_24,
            dialogEventListener = object : DialogEventListener {
                override fun onPositiveButtonClick() {
                    dataViewModel.deletePlaylist(playlist = receivedPlaylist)
                    findNavController().popBackStack()
                }
            }
        )
    }

    private fun navigateToEditPlaylist() = findNavController().navigate(
        TrackListFragmentDirections.actionTrackListFragmentToCreatePlaylistFragment(
            receivedPlaylist,
            Action.Update
        )
    )
}