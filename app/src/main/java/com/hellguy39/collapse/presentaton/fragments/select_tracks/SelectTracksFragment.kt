package com.hellguy39.collapse.presentaton.fragments.select_tracks

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.platform.MaterialSharedAxis
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.SelectTracksFragmentBinding
import com.hellguy39.collapse.presentaton.activities.main.MainActivity
import com.hellguy39.collapse.presentaton.adapters.SelectableTracksAdapter
import com.hellguy39.collapse.presentaton.view_models.MediaLibraryDataViewModel
import com.hellguy39.collapse.utils.getTrackItemVerticalDivider
import com.hellguy39.collapse.utils.getVerticalLayoutManager
import com.hellguy39.collapse.utils.setMaterialFadeThoughtAnimations
import com.hellguy39.collapse.utils.setOnBackFragmentNavigation
import com.hellguy39.domain.models.Track
import com.hellguy39.domain.usecases.GetImageBitmapUseCase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SelectTracksFragment : Fragment(R.layout.select_tracks_fragment),
    SelectableTracksAdapter.OnSelectableTrackListener {

    @Inject
    lateinit var getImageBitmapUseCase: GetImageBitmapUseCase

    companion object {
        fun newInstance() = SelectTracksFragment()
    }

    private lateinit var binding: SelectTracksFragmentBinding

    private lateinit var dataViewModel: MediaLibraryDataViewModel

    private var allTracks = mutableListOf<Track>()
    private var positions = mutableListOf<Int>()

    private val args: SelectTracksFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setMaterialFadeThoughtAnimations()

        dataViewModel = ViewModelProvider(activity as MainActivity)[MediaLibraryDataViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SelectTracksFragmentBinding.bind(view)

        postponeEnterTransition()

        binding.topAppBar.setOnBackFragmentNavigation(findNavController())

        setupRecyclerView()
        setObservers()
    }

    private fun setupRecyclerView() = binding.rvTracks.apply {
        addItemDecoration(this.getTrackItemVerticalDivider(requireContext()))
        layoutManager = getVerticalLayoutManager(requireContext())
        doOnPreDraw {
            startPostponedEnterTransition()
        }
        adapter = SelectableTracksAdapter(
            tracks = allTracks,
            getImageBitmapUseCase = getImageBitmapUseCase,
            listener = this@SelectTracksFragment
        )
    }

    private fun setObservers() {
        dataViewModel.getAllTracks().observe(viewLifecycleOwner) { receivedTracks ->
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

        val selectedTracks = args.selectedTracks.trackList

        for (n in selectedTracks.indices) {
            if (allTracks.contains(selectedTracks[n])) {
                (binding.rvTracks.adapter as SelectableTracksAdapter)
                    .addSelectedPosition(allTracks.indexOf(selectedTracks[n]))
            }
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
            returnableList.add(
                (binding.rvTracks.adapter as SelectableTracksAdapter)
                    .getTrackByPosition(positions[n])
            )
        }

        return returnableList
    }

    override fun onPause() {
        super.onPause()
        setResult()
    }

    private fun setResult() {
        setFragmentResult("pick_tracks",
            bundleOf("tracks" to collectTracks(positions = positions)))
    }

}