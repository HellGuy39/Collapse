package com.hellguy39.collapse.presentaton.fragments.radio

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.RadioFragmentBinding
import com.hellguy39.collapse.presentaton.adapters.RadioStationsAdapter
import com.hellguy39.collapse.presentaton.services.PlayerService
import com.hellguy39.domain.models.RadioStation
import com.hellguy39.domain.models.ServiceContentWrapper
import com.hellguy39.domain.utils.PlayerType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RadioFragment : Fragment(R.layout.radio_fragment), RadioStationsAdapter.OnRadioStationListener {

    companion object {
        fun newInstance() = RadioFragment()
    }

    private lateinit var viewModel: RadioViewModel
    private lateinit var binding: RadioFragmentBinding
    private var stations: MutableList<RadioStation> = mutableListOf()
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[RadioViewModel::class.java]
        viewModel.updateRadioStationList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = RadioFragmentBinding.bind(view)

        binding.rvStations.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = RadioStationsAdapter(stations = stations, this@RadioFragment)
        }

        val searchItem = binding.topAppBar.menu.findItem(R.id.search)

        searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {

                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {

                return false
            }

        })


        binding.topAppBar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.search -> {
                    true
                }
                R.id.add -> {
                    findNavController().navigate(R.id.addRadioStationFragment)
                    true
                }
                R.id.filter -> {
                    true
                }
                else -> false
            }
        }

        setObservers()
    }

    private fun setObservers() {
        viewModel.getRadioStationList().observe(viewLifecycleOwner) { receivedStations ->
            updateRvStations(receivedStations = receivedStations)
        }
    }

    private fun updateRvStations(receivedStations: List<RadioStation>) {
        clearRecyclerView()

        for (n in receivedStations.indices) {
            stations.add(receivedStations[n])
        }

        val position = binding.rvStations.adapter?.itemCount ?: 0
        binding.rvStations.adapter?.notifyItemInserted(position)
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

    override fun onDestroyView() {
        super.onDestroyView()
        searchView.setOnQueryTextListener(null)
    }
}