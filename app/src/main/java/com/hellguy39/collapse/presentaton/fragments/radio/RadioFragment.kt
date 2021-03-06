package com.hellguy39.collapse.presentaton.fragments.radio

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.RadioFragmentBinding
import com.hellguy39.collapse.presentaton.activities.main.MainActivity
import com.hellguy39.collapse.presentaton.adapters.RadioStationsAdapter
import com.hellguy39.collapse.presentaton.services.PlayerService
import com.hellguy39.collapse.presentaton.view_models.RadioStationsDataViewModel
import com.hellguy39.collapse.utils.*
import com.hellguy39.collapse.utils.getGridLayoutManager
import com.hellguy39.domain.models.RadioStation
import com.hellguy39.domain.models.ServiceContentWrapper
import com.hellguy39.domain.utils.PlayerType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RadioFragment : Fragment(R.layout.radio_fragment),
    RadioStationsAdapter.OnRadioStationListener,
    SearchView.OnQueryTextListener {

    companion object {
        fun newInstance() = RadioFragment()
    }

    private lateinit var dataViewModel: RadioStationsDataViewModel
    private lateinit var binding: RadioFragmentBinding
    private var stations: MutableList<RadioStation> = mutableListOf()
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setMaterialFadeThoughtAnimation()
        dataViewModel = ViewModelProvider(activity as MainActivity)[RadioStationsDataViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = RadioFragmentBinding.bind(view)

        postponeEnterTransition()

        setupRecyclerView()

        binding.fabAdd.setOnClickListener {
            navigateToAddNewRadioStation()
        }

        val searchItem = binding.topAppBar.menu.findItem(R.id.search)

        searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(this)

        setObservers()

        binding.emptyView.btnAction.apply {
            text = "Add new radio station"
            setOnClickListener {
                navigateToAddNewRadioStation()
            }
        }
    }

    private fun setupRecyclerView() = binding.rvStations.apply {
        doOnPreDraw {
            startPostponedEnterTransition()
        }
        layoutManager = getGridLayoutManager(requireContext())
        adapter = RadioStationsAdapter(
            stations = stations,
            listener = this@RadioFragment,
            context = context
        )
    }

    private fun setObservers() {
        dataViewModel.getRadioStationList().observe(viewLifecycleOwner) { receivedStations ->
            updateRecyclerView(receivedStations = receivedStations)
        }
    }

    private fun updateRecyclerView(receivedStations: List<RadioStation>) {
        clearRecyclerView()

        for (n in receivedStations.indices) {
            stations.add(receivedStations[n])
        }

        val position = binding.rvStations.adapter?.itemCount ?: 0
        binding.rvStations.adapter?.notifyItemInserted(position)
        checkEmptyView()
    }

    private fun checkEmptyView() {
        binding.emptyView.root.isVisible = binding.rvStations.adapter?.itemCount == 0
    }

    private fun clearRecyclerView() {
        val size = binding.rvStations.adapter?.itemCount
        stations.clear()
        binding.rvStations.adapter?.notifyItemRangeRemoved(0, size ?: 0)
    }


    override fun onStationClick(pos: Int) {
        PlayerService.startService(requireContext(), ServiceContentWrapper(
                type = PlayerType.Radio,
                radioStation = stations[pos]
            )
        )
    }

    override fun onStationDelete(radioStation: RadioStation) {
        showDeleteDialog(radioStation)
    }

    override fun onStationEdit(radioStation: RadioStation) {
        findNavController().navigate(
            RadioFragmentDirections.actionRadioFragmentToAddRadioStationFragment(
                Action.Update,
                radioStation
            )
        )
    }

    private fun showDeleteDialog(radioStation: RadioStation) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Are you sure want to delete this station?")
            .setNeutralButton("Cancel") { dialog, which ->
                dialog.cancel()
            }
            .setPositiveButton("Yes") { dialog, which ->
                dataViewModel.deleteRadioStation(radioStation = radioStation)
            }
            .show()
    }

    private fun navigateToAddNewRadioStation() {
        findNavController().navigate(
            RadioFragmentDirections.actionRadioFragmentToAddRadioStationFragment(
                Action.Create,
                RadioStation()
            ), FragmentNavigatorExtras(binding.fabAdd to "create_radio_station_transition")
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchView.setOnQueryTextListener(null)
    }

    private fun onSearchViewChangeQuery(query: String?) {

        val queryList: List<RadioStation> = dataViewModel.searchWithQueryInRadioStations(
            query = query?: "",
            dataViewModel.getRadioStationList().value
        )

        clearRecyclerView()
        updateRecyclerView(queryList)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        onSearchViewChangeQuery(query = query)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        onSearchViewChangeQuery(query = newText)
        return false
    }
}