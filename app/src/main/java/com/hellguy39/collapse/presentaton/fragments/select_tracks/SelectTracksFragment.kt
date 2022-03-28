package com.hellguy39.collapse.presentaton.fragments.select_tracks

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.SelectTracksFragmentBinding
import com.hellguy39.collapse.presentaton.adapters.SelectableTracksAdapter
import com.hellguy39.collapse.presentaton.fragments.create_playlist.CreatePlaylistFragment
import com.hellguy39.collapse.presentaton.fragments.create_playlist.CreatePlaylistViewModel
import com.hellguy39.domain.models.Track
import com.hellguy39.domain.usecases.GetImageBitmapUseCase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SelectTracksFragment : Fragment(R.layout.select_tracks_fragment),
    SelectableTracksAdapter.OnSelectableTrackListener, View.OnClickListener {

    @Inject
    lateinit var getImageBitmapUseCase: GetImageBitmapUseCase

    companion object {
        fun newInstance() = SelectTracksFragment()
    }

    private lateinit var viewModel: SelectTracksViewModel
    private val createPlaylistViewModel: CreatePlaylistViewModel by viewModels()
    private lateinit var binding: SelectTracksFragmentBinding

    private lateinit var adapter: SelectableTracksAdapter
    private var allTracks = mutableListOf<Track>()
    private var positions = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[SelectTracksViewModel::class.java]
        adapter = SelectableTracksAdapter(
            tracks = allTracks,
            getImageBitmapUseCase = getImageBitmapUseCase,
            listener = this
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SelectTracksFragmentBinding.bind(view)

        binding.topAppBar.setNavigationOnClickListener { findNavController().popBackStack() }

        binding.rvTracks.adapter = adapter
        binding.rvTracks.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )

        setObservers()
    }

    private fun setObservers() {
        viewModel.getTrackList().observe(viewLifecycleOwner) { receivedTracks ->
            clearRecyclerView()
            updateRecyclerView(receivedTracks)
        }
    }

    private fun clearRecyclerView() {
        val size = binding.rvTracks.adapter?.itemCount
        allTracks.clear()
        binding.rvTracks.adapter?.notifyItemRangeRemoved(0, size ?: 0)
    }

    private fun updateRecyclerView(receivedTracks: List<Track>) {
        for (n in receivedTracks.indices) {
            allTracks.add(receivedTracks[n])
        }

        val position = binding.rvTracks.adapter?.itemCount ?: 0
        binding.rvTracks.adapter?.notifyItemRangeInserted(position, allTracks.size)
    }

    override fun onSelectTrack(position: Int) {
        positions.add(position)
        updateTitleCounter()
    }

    override fun onUnselectTrack(position: Int) {
        positions.removeAt(index = positions.binarySearch(position))
        updateTitleCounter()
    }

    private fun updateTitleCounter() { binding.topAppBar.title = "${positions.size} selected"}

    private fun collectTracks(positions: List<Int>): List<Track> {

        val returnableList = mutableListOf<Track>()

        for (n in positions.indices) {
            returnableList.add(adapter.getTrackByPosition(positions[n]))
        }

        return returnableList
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            R.id.fabConfirm -> {
                createPlaylistViewModel.updateTrackList(collectTracks(positions = positions))
                findNavController().popBackStack()
            }
        }
    }
}