package com.hellguy39.collapse.presentaton.fragments.radio

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.RadioFragmentBinding
import com.hellguy39.collapse.presentaton.adapters.RadioStationListAdapter
import com.hellguy39.collapse.presentaton.services.PlayerService
import com.hellguy39.domain.models.RadioStation
import com.hellguy39.domain.models.ServiceContentWrapper
import com.hellguy39.domain.utils.PlayerType

class RadioFragment : Fragment(R.layout.radio_fragment), RadioStationListAdapter.OnRadioStationListener {

    companion object {
        fun newInstance() = RadioFragment()
    }

    private lateinit var viewModel: RadioViewModel
    private lateinit var binding: RadioFragmentBinding
    private var stations: MutableList<RadioStation> = mutableListOf()
    private lateinit var searchView: SearchView

    /*override fun onAttach(context: Context) {
        super.onAttach(context)
        setHasOptionsMenu(true)
    }
*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[RadioViewModel::class.java]

        val st1 = RadioStation(
            name = "BBC 1",
            url = "http://stream.live.vc.bbcmedia.co.uk/bbc_radio_one",
        )

        stations.add(st1)
        stations.add(st1)
        stations.add(st1)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = RadioFragmentBinding.bind(view)

        binding.rvStations.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = RadioStationListAdapter(stations = stations, this@RadioFragment)
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
                R.id.filter -> {
                    true
                }
                else -> false
            }
        }
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