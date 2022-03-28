package com.hellguy39.collapse.presentaton.fragments.add_radio_station

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.AddRadioStationFragmentBinding
import com.hellguy39.domain.models.RadioStation
import com.hellguy39.domain.utils.Protocol
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddRadioStationFragment : Fragment(R.layout.add_radio_station_fragment) {

    companion object {
        fun newInstance() = AddRadioStationFragment()
    }

    private lateinit var viewModel: AddRadioStationViewModel
    private lateinit var binding: AddRadioStationFragmentBinding

    private val protocols = listOf<Enum<Protocol>>(
        Protocol.HLS,
        Protocol.SmoothStreaming,
        Protocol.RTSP,
        Protocol.Progressive,
        Protocol.DASH,
    )
    private var selectedProtocol = Protocol.HLS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[AddRadioStationViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = AddRadioStationFragmentBinding.bind(view)

        binding.fabAdd.hide()

        binding.fabAdd.setOnClickListener {
            if (checkEditTexts()) {
                viewModel.addNewRadioStation(RadioStation(
                    name = getNameText(),
                    url = getUrlText(),
                    protocol = selectedProtocol
                ))
                findNavController().popBackStack()
            }
        }

        binding.etName.doOnTextChanged { text, start, before, count ->
            updateExFab()
        }

        binding.etUrl.doOnTextChanged { text, start, before, count ->
            updateExFab()
        }

        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, protocols)
        binding.acProtocol.setAdapter(adapter)
        binding.acProtocol.setText(binding.acProtocol.adapter.getItem(0).toString(), false)
        binding.acProtocol.setOnItemClickListener { adapterView, view, i, l ->
            when (binding.acProtocol.adapter.getItem(i).toString()) {
                Protocol.HLS.name -> selectedProtocol = Protocol.HLS
                Protocol.DASH.name -> selectedProtocol = Protocol.DASH
                Protocol.SmoothStreaming.name -> selectedProtocol = Protocol.SmoothStreaming
                Protocol.RTSP.name -> selectedProtocol = Protocol.RTSP
                Protocol.Progressive.name -> selectedProtocol = Protocol.Progressive
            }
        }
    }

    private fun updateExFab() {
        if (!checkEditTexts()) {
            if (binding.fabAdd.isVisible)
                binding.fabAdd.hide()
        } else
            binding.fabAdd.show()
    }

    private fun checkEditTexts(): Boolean {
        return getUrlText().isNotEmpty() && getNameText().isNotEmpty()
    }

    private fun getNameText(): String = binding.etName.text.toString()

    private fun getUrlText(): String = binding.etUrl.text.toString()

}