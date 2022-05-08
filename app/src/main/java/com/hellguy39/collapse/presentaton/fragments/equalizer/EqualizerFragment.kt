package com.hellguy39.collapse.presentaton.fragments.equalizer

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.slider.Slider
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.EqualizerFragmentBinding
import com.hellguy39.collapse.utils.AudioEffectController
import com.hellguy39.collapse.utils.setMaterialFadeThoughtAnimations
import com.hellguy39.domain.usecases.GetColorFromThemeUseCase
import com.hellguy39.domain.usecases.eq_settings.EqualizerSettingsUseCases
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EqualizerFragment : Fragment(R.layout.equalizer_fragment),
    Slider.OnChangeListener,
    Slider.OnSliderTouchListener,
    CompoundButton.OnCheckedChangeListener {

    @Inject
    lateinit var getColorFromThemeUseCase: GetColorFromThemeUseCase

    @Inject
    lateinit var effectController: AudioEffectController

    @Inject
    lateinit var getEqualizerSettingsUseCases: EqualizerSettingsUseCases

    companion object {
        fun newInstance() = EqualizerFragment()
        private const val STEP_SIZE = 1f

        val reverbPresetNames = listOf(
            "None",
            "Small room",
            "Medium room",
            "Large room",
            "Medium hall",
            "Large hall",
            "Plate"
        )
    }

    private lateinit var binding: EqualizerFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setMaterialFadeThoughtAnimations()

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.fragmentContainer
            setAllContainerColors(getColorFromThemeUseCase.invoke(
                requireActivity().theme,
                com.google.android.material.R.attr.colorSurface
            ))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = EqualizerFragmentBinding.bind(view)

        binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        setupUI()
        displayAudioEffectControllerValues()
    }

    private fun setupUI() {
        val properties = effectController.getProperties()

        val upperBandLevel = properties.upperBandLevel
        val lowestBandLevel = properties.lowestBandLevel
        val bandsCenterFreq = properties.bandsCenterFreq

        binding.tvDbMax.text = (upperBandLevel / 100).toString() + " dB"
        binding.tvDbMin.text = (lowestBandLevel / 100).toString() + " dB"

        binding.tvBand1CenterFreq.text = formatBandFreq(bandsCenterFreq[0])
        binding.tvBand2CenterFreq.text = formatBandFreq(bandsCenterFreq[1])
        binding.tvBand3CenterFreq.text = formatBandFreq(bandsCenterFreq[2])
        binding.tvBand4CenterFreq.text = formatBandFreq(bandsCenterFreq[3])
        binding.tvBand5CenterFreq.text = formatBandFreq(bandsCenterFreq[4])

        binding.eqSwitch.setOnCheckedChangeListener(this)
        binding.virtualizerSwitch.setOnCheckedChangeListener(this)
        binding.bassSwitch.setOnCheckedChangeListener(this)
        binding.reverbSwitch.setOnCheckedChangeListener(this)

        binding.band1.apply {
            valueFrom = (lowestBandLevel / 100).toFloat()
            valueTo = (upperBandLevel / 100).toFloat()
            stepSize = STEP_SIZE
            addOnChangeListener(this@EqualizerFragment)
            addOnSliderTouchListener(this@EqualizerFragment)
        }
        binding.band2.apply {
            valueFrom = (lowestBandLevel / 100).toFloat()
            valueTo = (upperBandLevel / 100).toFloat()
            stepSize = STEP_SIZE
            addOnChangeListener(this@EqualizerFragment)
            addOnSliderTouchListener(this@EqualizerFragment)
        }
        binding.band3.apply {
            valueFrom = (lowestBandLevel / 100).toFloat()
            valueTo = (upperBandLevel / 100).toFloat()
            stepSize = STEP_SIZE
            addOnChangeListener(this@EqualizerFragment)
            addOnSliderTouchListener(this@EqualizerFragment)
        }
        binding.band4.apply {
            valueFrom = (lowestBandLevel / 100).toFloat()
            valueTo = (upperBandLevel / 100).toFloat()
            stepSize = STEP_SIZE
            addOnChangeListener(this@EqualizerFragment)
            addOnSliderTouchListener(this@EqualizerFragment)
        }
        binding.band5.apply {
            valueFrom = (lowestBandLevel / 100).toFloat()
            valueTo = (upperBandLevel / 100).toFloat()
            stepSize = STEP_SIZE
            addOnChangeListener(this@EqualizerFragment)
            addOnSliderTouchListener(this@EqualizerFragment)
        }
        if (properties.bassBoostSupport) {
            binding.bassBoostBand.apply {
                valueFrom = 0f
                valueTo = 10f
                stepSize = STEP_SIZE
                addOnChangeListener(this@EqualizerFragment)
                addOnSliderTouchListener(this@EqualizerFragment)
            }
        } else {
            binding.bassSwitch.isEnabled = false
            binding.bassBoostBand.isEnabled = false
        }
        if (properties.virtualizerSupport) {
            binding.surroundBand.apply {
                valueFrom = 0f
                valueTo = 10f
                stepSize = STEP_SIZE
                addOnChangeListener(this@EqualizerFragment)
                addOnSliderTouchListener(this@EqualizerFragment)
            }
        } else {
            binding.eqSwitch.isEnabled = false
            binding.surroundBand.isEnabled = false
        }

        setupPresetChips()
        setupReverbChips()

//        binding.acReverb.setOnItemClickListener { adapterView, view, i, l ->
//            val item = binding.acReverb.adapter.getItem(i).toString()
//            val presets = effectController.getReverbPresetList()
//
//            when(item) {
//                reverbPresetNames[0] -> effectController.setReverbPreset(presets[0])
//                reverbPresetNames[1] -> effectController.setReverbPreset(presets[1])
//                reverbPresetNames[2] -> effectController.setReverbPreset(presets[2])
//                reverbPresetNames[3] -> effectController.setReverbPreset(presets[3])
//                reverbPresetNames[4] -> effectController.setReverbPreset(presets[4])
//                reverbPresetNames[5] -> effectController.setReverbPreset(presets[5])
//                reverbPresetNames[6] -> effectController.setReverbPreset(presets[6])
//            }
//        }

    }

    private fun displayAudioEffectControllerValues() {
        val settings = effectController.getCurrentSettings().value ?: return

        binding.eqSwitch.isChecked = settings.isEqEnabled
        binding.bassSwitch.isChecked = settings.isBassEnabled
        binding.virtualizerSwitch.isChecked = settings.isVirtualizerEnabled

        binding.band1.value = (settings.band1Level / 100).toFloat()
        binding.band2.value = (settings.band2Level / 100).toFloat()
        binding.band3.value = (settings.band3Level / 100).toFloat()
        binding.band4.value = (settings.band4Level / 100).toFloat()
        binding.band5.value = (settings.band5Level / 100).toFloat()

        if (binding.bassBoostBand.isEnabled)
            binding.bassBoostBand.value = (settings.bandBassBoost / 100).toFloat()

        if (binding.surroundBand.isEnabled)
            binding.surroundBand.value = (settings.bandVirtualizer / 100).toFloat()
    }

    private fun formatBandFreq(freq: Int): String =
        if (freq > 1000)
            "${freq.toDouble() / 1000} kHz"
        else
            "$freq Hz"

    private fun setupPresetChips() {
        val presets = effectController.getProperties().presets

        binding.presetChipGroup.isSingleSelection = true

        for (preset in presets) {
            val chip: Chip = Chip(requireContext())

            chip.isCheckable = true
            chip.tag = preset.presetNumber
            chip.text = preset.name

            chip.setOnCheckedChangeListener { compoundButton, isChecked ->
                onEqPresetChange(compoundButton.tag.toString().toShort())
            }

            binding.presetChipGroup.addView(chip)
        }
    }

    private fun setupReverbChips() {

        binding.reverbChipGroup.isSingleSelection = true

        for(reverbPreset in reverbPresetNames) {
            val chip: Chip = Chip(requireContext())

            chip.isCheckable = true
            chip.text = reverbPreset
            chip.tag = reverbPreset

            chip.setOnCheckedChangeListener { compoundButton, isChecked ->
                onReverbPresetChange(compoundButton.tag.toString())
            }

            binding.reverbChipGroup.addView(chip)
        }
    }

    private fun onEqPresetChange(presetNumber: Short) {
        effectController.setPreset(presetNumber)
    }

    private fun onReverbPresetChange(reverbPreset: String) {

    }

    private fun onSliderChangeValue(slider: Slider, value: Float, fromUser: Boolean) {
        when (slider.id) {
            R.id.band1 -> {
                effectController.setBandLevel(0,(value * 100).toInt().toShort())
            }
            R.id.band2 -> {
                effectController.setBandLevel(1,(value * 100).toInt().toShort())
            }
            R.id.band3 -> {
                effectController.setBandLevel(2,(value * 100).toInt().toShort())
            }
            R.id.band4 -> {
                effectController.setBandLevel(3,(value * 100).toInt().toShort())
            }
            R.id.band5 -> {
                effectController.setBandLevel(4,(value * 100).toInt().toShort())
            }
            R.id.bassBoostBand -> {
                effectController.setBassStrength((value * 100).toInt().toShort())
            }
            R.id.surroundBand -> {
                effectController.setVirtualizeStrength((value * 100).toInt().toShort())
            }
        }
    }

    @SuppressLint("RestrictedApi")
    override fun onValueChange(slider: Slider, value: Float, fromUser: Boolean) {
        onSliderChangeValue(slider, value, fromUser)
    }

    @SuppressLint("RestrictedApi")
    override fun onStartTrackingTouch(slider: Slider) {
        onSliderChangeValue(slider, slider.value, true)
    }

    @SuppressLint("RestrictedApi")
    override fun onStopTrackingTouch(slider: Slider) {
        onSliderChangeValue(slider, slider.value, true)
    }

    override fun onCheckedChanged(p0: CompoundButton?, b: Boolean) {
        when(p0?.id) {
            binding.eqSwitch.id -> effectController.setEqEnabled(b)
            binding.bassSwitch.id -> effectController.setBassEnabled(b)
            binding.virtualizerSwitch.id -> effectController.setVirtualizeEnabled(b)
            binding.reverbSwitch.id -> effectController.setReverbEnabled(b)
        }
    }
}