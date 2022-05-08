package com.hellguy39.collapse.presentaton.fragments.add_radio_station

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.platform.MaterialArcMotion
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.AddRadioStationFragmentBinding
import com.hellguy39.collapse.presentaton.activities.main.MainActivity
import com.hellguy39.collapse.presentaton.view_models.RadioStationsDataViewModel
import com.hellguy39.collapse.utils.Action
import com.hellguy39.collapse.utils.setMaterialFadeThoughtAnimations
import com.hellguy39.collapse.utils.setOnBackFragmentNavigation
import com.hellguy39.domain.models.RadioStation
import com.hellguy39.domain.usecases.ConvertBitmapToByteArrayUseCase
import com.hellguy39.domain.usecases.ConvertByteArrayToBitmapUseCase
import com.hellguy39.domain.usecases.GetColorFromThemeUseCase
import com.hellguy39.domain.utils.Protocol
import dagger.hilt.android.AndroidEntryPoint
import java.io.InputStream
import javax.inject.Inject

@AndroidEntryPoint
class AddRadioStationFragment : Fragment(R.layout.add_radio_station_fragment) {

    @Inject
    lateinit var convertByteArrayToBitmapUseCase: ConvertByteArrayToBitmapUseCase

    @Inject
    lateinit var convertBitmapToByteArrayUseCase: ConvertBitmapToByteArrayUseCase

    @Inject
    lateinit var getColorFromThemeUseCase: GetColorFromThemeUseCase

    companion object {
        fun newInstance() = AddRadioStationFragment()
    }

    private lateinit var dataViewModel: RadioStationsDataViewModel
    private lateinit var binding: AddRadioStationFragmentBinding

    private lateinit var pickImage: ActivityResultLauncher<String>
    private var selectedImage: Bitmap? = null

    private val args: AddRadioStationFragmentArgs by navArgs()

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

        setMaterialFadeThoughtAnimations()

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.fragmentContainer
            //scrimColor = Color.TRANSPARENT
            setAllContainerColors(
                getColorFromThemeUseCase.invoke(
                    requireActivity().theme,
                    com.google.android.material.R.attr.colorSurface
                )
            )
            pathMotion = MaterialArcMotion()
        }

        dataViewModel = ViewModelProvider(activity as MainActivity)[RadioStationsDataViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = AddRadioStationFragmentBinding.bind(view)

        binding.fabAdd.hide()

        binding.topAppBar.setOnBackFragmentNavigation(findNavController())

        pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
            if (it != null) {
                val imageStream: InputStream? = context?.contentResolver?.openInputStream(it)
                selectedImage = BitmapFactory.decodeStream(imageStream)
                binding.ivImage.setImageBitmap(selectedImage)
            }
        }

        binding.fabAdd.setOnClickListener {
            if (checkEditTexts()) {
                if (args.action == Action.Create) {
                    dataViewModel.addNewRadioStation(RadioStation(
                        name = getNameText(),
                        url = getUrlText(),
                        protocol = selectedProtocol,
                        picture = convertBitmapToByteArrayUseCase.invoke(selectedImage)
                    ))
                } else if (args.action == Action.Update) {
                    dataViewModel.updateRadioStation(
                        RadioStation(
                            id = args.radioStation.id,
                            name = getNameText(),
                            url = getUrlText(),
                            protocol = selectedProtocol,
                            picture = convertBitmapToByteArrayUseCase.invoke(selectedImage)
                        )
                    )
                }
                findNavController().popBackStack()
            }
        }

        binding.ivImage.setOnClickListener {
            pickImage.launch("image/*")
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
        binding.acProtocol.setOnItemClickListener { _, _, i, _ ->
            when (binding.acProtocol.adapter.getItem(i).toString()) {
                Protocol.HLS.name -> selectedProtocol = Protocol.HLS
                Protocol.DASH.name -> selectedProtocol = Protocol.DASH
                Protocol.SmoothStreaming.name -> selectedProtocol = Protocol.SmoothStreaming
                Protocol.RTSP.name -> selectedProtocol = Protocol.RTSP
                Protocol.Progressive.name -> selectedProtocol = Protocol.Progressive
            }
        }

        when(args.action) {
            Action.Update -> {
                binding.topAppBar.title = "Edit"

                binding.etName.setText(args.radioStation.name)
                binding.etUrl.setText(args.radioStation.url)

                val bytes = args.radioStation.picture
                if (bytes != null) {
                    val bitmap = convertByteArrayToBitmapUseCase.invoke(bytes)
                    binding.ivImage.setImageBitmap(bitmap)
                    selectedImage = bitmap
                } else
                    binding.ivImage.setImageResource(R.drawable.ic_round_radio_24)

                when(args.radioStation.protocol) {
                    Protocol.HLS -> {
                        binding.acProtocol.setText(binding.acProtocol.adapter.getItem(0).toString(), false)
                        selectedProtocol = Protocol.HLS
                    }
                    Protocol.SmoothStreaming -> {
                        binding.acProtocol.setText(binding.acProtocol.adapter.getItem(1).toString(), false)
                        selectedProtocol = Protocol.SmoothStreaming
                    }
                    Protocol.RTSP -> {
                        binding.acProtocol.setText(binding.acProtocol.adapter.getItem(2).toString(), false)
                        selectedProtocol = Protocol.RTSP
                    }
                    Protocol.Progressive -> {
                        binding.acProtocol.setText(binding.acProtocol.adapter.getItem(3).toString(), false)
                        selectedProtocol = Protocol.Progressive
                    }
                    Protocol.DASH -> {
                        binding.acProtocol.setText(binding.acProtocol.adapter.getItem(4).toString(), false)
                        selectedProtocol = Protocol.DASH
                    }
                }
            }
            Action.Create -> {
                binding.topAppBar.title = "New radio station"
            }
            else -> {}
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