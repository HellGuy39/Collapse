package com.hellguy39.collapse.presentaton.fragments.add_radio_station
//
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.os.Bundle
//import android.view.View
//import androidx.activity.result.ActivityResultLauncher
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.core.view.forEach
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.ViewModelProvider
//import androidx.navigation.fragment.findNavController
//import androidx.navigation.fragment.navArgs
//import com.google.android.material.chip.Chip
//import com.google.android.material.snackbar.Snackbar
//import com.hellguy39.collapse.R
//import com.hellguy39.collapse.databinding.AddRadioStationFragmentBinding
//import com.hellguy39.collapse.presentaton.activities.main.MainActivity
//import com.hellguy39.collapse.presentaton.view_models.RadioStationsDataViewModel
//import com.hellguy39.collapse.utils.*
//import com.hellguy39.domain.models.RadioStation
//import com.hellguy39.domain.utils.Protocol
//import dagger.hilt.android.AndroidEntryPoint
//import java.io.InputStream
//
//@AndroidEntryPoint
//class AddRadioStationFragment : Fragment(R.layout.add_radio_station_fragment), View.OnClickListener {
//
//    companion object {
//        fun newInstance() = AddRadioStationFragment()
//
//        val protocols = listOf<Enum<Protocol>>(
//            Protocol.HLS,
//            Protocol.SmoothStreaming,
//            Protocol.RTSP,
//            Protocol.Progressive,
//            Protocol.DASH,
//        )
//    }
//
//    private lateinit var dataViewModel: RadioStationsDataViewModel
//    private lateinit var binding: AddRadioStationFragmentBinding
//
//    private lateinit var pickImage: ActivityResultLauncher<String>
//    private var selectedImage: Bitmap? = null
//
//    private val args: AddRadioStationFragmentArgs by navArgs()
//    private var selectedProtocol = Protocol.HLS
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        setMaterialFadeThoughtAnimation()
//
//        setSharedElementTransitionAnimation()
//
//        dataViewModel = ViewModelProvider(activity as MainActivity)[RadioStationsDataViewModel::class.java]
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        binding = AddRadioStationFragmentBinding.bind(view)
//
//        binding.topAppBar.setOnBackFragmentNavigation()
//
//        binding.btnCreate.setOnClickListener(this)
//
//        setupPickImageListener()
//
//        setupProtocolChipGroup().also {
//            //Default selection
//            setChipSelected(Protocol.HLS)
//        }
//
//        when(args.action) {
//            Action.Update -> {
//                binding.topAppBar.title = "Edit"
//
//                binding.etName.setText(args.radioStation.name)
//                binding.etUrl.setText(args.radioStation.url)
//
//                val bytes = args.radioStation.picture
//                if (bytes != null) {
//                    bytes.toBitmap().also { bitmap ->
//                        binding.ivImage.setImageBitmap(bitmap)
//                        selectedImage = bitmap
//                    }
//                } else
//                    binding.ivImage.setImageResource(R.drawable.ic_round_radio_24)
//
//                setChipSelected(args.radioStation.protocol)
//            }
//            Action.Create -> {
//                binding.topAppBar.title = "New radio station"
//            }
//            else -> {}
//        }
//    }
//
//    private fun setupProtocolChipGroup() {
//        for (protocol in protocols) {
//            val chip = Chip(requireContext())
//
//            chip.text = protocol.name
//            chip.tag = protocol.name
//
//            chip.isCheckable = true
//
//            chip.setOnCheckedChangeListener { compoundButton, b ->
//
//                if (!b)
//                    return@setOnCheckedChangeListener
//
//                selectedProtocol = when(compoundButton.tag) {
//                    Protocol.HLS.name -> Protocol.HLS
//                    Protocol.DASH.name -> Protocol.DASH
//                    Protocol.Progressive.name -> Protocol.Progressive
//                    Protocol.RTSP.name -> Protocol.RTSP
//                    Protocol.SmoothStreaming.name -> Protocol.SmoothStreaming
//                    else -> return@setOnCheckedChangeListener
//                }
//            }
//
//            binding.protocolChipGroup.addView(chip)
//        }
//    }
//
//    private fun setupPickImageListener() {
//        pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
//            if (it != null) {
//                val imageStream: InputStream? = context?.contentResolver?.openInputStream(it)
//                selectedImage = BitmapFactory.decodeStream(imageStream)
//                binding.ivImage.setImageBitmap(selectedImage)
//            }
//        }
//
//        binding.ivImage.setOnClickListener {
//            pickImage.launch("image/*")
//        }
//    }
//
//    private fun setChipSelected(protocol: Enum<Protocol>) {
//        binding.protocolChipGroup.forEach { view ->
//            if (view.tag == protocol.name) {
//                (view as Chip).isChecked = true
//                return@forEach
//            }
//        }
//    }
//
//    private fun checkEditTexts(): Boolean {
//        return getUrlText().isNotEmpty() && getNameText().isNotEmpty()
//    }
//
//    private fun getNameText(): String = binding.etName.text.toString()
//
//    private fun getUrlText(): String = binding.etUrl.text.toString()
//
//    override fun onClick(view: View?) {
//        when(view?.id) {
//            binding.btnCreate.id -> {
//                if (checkEditTexts()) {
//                    if (args.action == Action.Create) {
//                        onAddNewRadioStation()
//                    } else if (args.action == Action.Update) {
//                        onUpdateRadioStation()
//                    }
//                    findNavController().popBackStack()
//                } else {
//                    Snackbar.make(
//                        binding.root,
//                        "Some text fields are empty",
//                        Snackbar.LENGTH_SHORT
//                    ).show()
//                }
//            }
//        }
//    }
//
//    private fun onAddNewRadioStation() = dataViewModel.addNewRadioStation(
//        RadioStation(
//            name = getNameText(),
//            url = getUrlText(),
//            protocol = selectedProtocol,
//            picture = selectedImage?.toByteArray()
//        )
//    )
//
//    private fun onUpdateRadioStation() = dataViewModel.updateRadioStation(
//        RadioStation(
//            id = args.radioStation.id,
//            name = getNameText(),
//            url = getUrlText(),
//            protocol = selectedProtocol,
//            picture = selectedImage?.toByteArray()
//        )
//    )
//
//}