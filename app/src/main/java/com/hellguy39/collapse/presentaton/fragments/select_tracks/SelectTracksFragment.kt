package com.hellguy39.collapse.presentaton.fragments.select_tracks

//import android.os.Bundle
//import android.util.Log
//import android.view.View
//import androidx.core.view.doOnPreDraw
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.activityViewModels
//import androidx.lifecycle.ViewModelProvider
//import com.hellguy39.collapse.R
//import com.hellguy39.collapse.databinding.SelectTracksFragmentBinding
//import com.hellguy39.collapse.presentaton.activities.main.MainActivity
//import com.hellguy39.collapse.presentaton.adapters.SelectableTracksAdapter
//import com.hellguy39.collapse.presentaton.view_models.MediaLibraryDataViewModel
//import com.hellguy39.collapse.presentaton.view_models.PlaylistSharedViewModel
//import com.hellguy39.collapse.utils.getTrackItemVerticalDivider
//import com.hellguy39.collapse.utils.getVerticalLayoutManager
//import com.hellguy39.collapse.utils.setMaterialFadeThoughtAnimation
//import com.hellguy39.collapse.utils.setOnBackFragmentNavigation
//import com.hellguy39.domain.models.Track
//import dagger.hilt.android.AndroidEntryPoint
//
//@AndroidEntryPoint
//class SelectTracksFragment : Fragment(R.layout.select_tracks_fragment),
//    SelectableTracksAdapter.OnSelectableTrackListener {
//
//    companion object {
//        fun newInstance() = SelectTracksFragment()
//    }
//
//    private lateinit var binding: SelectTracksFragmentBinding
//
//    private val sharedViewModel: PlaylistSharedViewModel by activityViewModels()
//
//    private lateinit var dataViewModel: MediaLibraryDataViewModel
//
//    private var allTracks = mutableListOf<Track>()
//    private var selectedTracks = mutableListOf<Track>()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        setMaterialFadeThoughtAnimation()
//
//        dataViewModel = ViewModelProvider(activity as MainActivity)[MediaLibraryDataViewModel::class.java]
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        binding = SelectTracksFragmentBinding.bind(view)
//
//        postponeEnterTransition()
//
//        binding.topAppBar.setOnBackFragmentNavigation()
//
//        allTracks = dataViewModel.getAllTracks().value?.toMutableList() ?: mutableListOf()
//        selectedTracks = sharedViewModel.getSelectedTracks().value ?: mutableListOf()
//
//        updateTitleCounter()
//
//        setupRecyclerView()
//        setObservers()
//    }
//
//    private fun setupRecyclerView() = binding.rvTracks.apply {
//        addItemDecoration(this.getTrackItemVerticalDivider(requireContext()))
//        layoutManager = getVerticalLayoutManager(requireContext())
//        doOnPreDraw {
//            startPostponedEnterTransition()
//        }
//        adapter = SelectableTracksAdapter(
//            tracks = allTracks,
//            selectedTracks = selectedTracks,
//            listener = this@SelectTracksFragment
//        )
//    }
//
//    private fun setObservers() {
//
////        clearRecyclerView()
////        updateRecyclerView()
//    }
////
////    private fun clearRecyclerView() {
////        val size = binding.rvTracks.adapter?.itemCount
////        allTracks.clear()
////        binding.rvTracks.adapter?.notifyItemRangeRemoved(0, size ?: 0)
////    }
////
////    private fun updateRecyclerView() {
////        val position = binding.rvTracks.adapter?.itemCount ?: 0
////        binding.rvTracks.adapter?.notifyItemRangeInserted(position, allTracks.size)
////    }
//
//    override fun onSelectTrack(track: Track) {
//        sharedViewModel.addTrack(track)
//        //selectedTracks.add(track)
//        updateTitleCounter()
//    }
//
//    override fun onUnselectTrack(track: Track) {
//        sharedViewModel.removeTrack(track)
//        selectedTracks.remove(track)
//        updateTitleCounter()
//    }
//
//    private fun updateTitleCounter() {
//        binding.topAppBar.title = "${selectedTracks.size} selected"
//    }
//
//}