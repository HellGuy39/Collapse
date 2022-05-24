package com.hellguy39.collapse.presentaton.fragments.equalizer

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import com.google.android.material.slider.Slider
import com.hellguy39.collapse.R
import com.hellguy39.collapse.controllers.audio_effect.AudioEffectController
import com.hellguy39.collapse.controllers.audio_effect.EqState
import com.hellguy39.collapse.databinding.EqualizerFragmentBinding
import com.hellguy39.collapse.utils.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.roundToInt

@AndroidEntryPoint
class EqualizerFragment : Fragment(R.layout.equalizer_fragment),
    Slider.OnChangeListener,
    Slider.OnSliderTouchListener,
    CompoundButton.OnCheckedChangeListener {

    @Inject
    lateinit var effectController: AudioEffectController

    companion object {
        fun newInstance() = EqualizerFragment()

        private const val STEP_SIZE = 1f
        private const val MAX_BASS_BOOST_VALUE = 10f
        private const val MIN_BASS_BOOST_VALUE = 0f
        private const val MAX_VIRTUALIZER_VALUE = 10f
        private const val MIN_VIRTUALIZER_VALUE = 0f

//        private val reverbPresetMap: Map<String, Short> = mutableMapOf(
//            "None" to PresetReverb.PRESET_NONE,
//            "Small room" to PresetReverb.PRESET_SMALLROOM,
//            "Medium room" to PresetReverb.PRESET_MEDIUMROOM,
//            "Large room" to PresetReverb.PRESET_LARGEROOM,
//            "Medium hall" to PresetReverb.PRESET_MEDIUMHALL,
//            "Large hall" to PresetReverb.PRESET_LARGEHALL,
//            "Plate" to PresetReverb.PRESET_PLATE
//        )
    }

    private lateinit var binding: EqualizerFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setMaterialFadeThoughtAnimation()

        setSharedElementTransitionAnimation()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = EqualizerFragmentBinding.bind(view)

        binding.topAppBar.setOnBackFragmentNavigation()

        setupUI()
        setObservers()
    }

    private fun setupUI() {
        val properties = effectController.getProperties()

        val upperBandLevel = properties.upperBandLevel
        val lowestBandLevel = properties.lowestBandLevel
        val bandsCenterFreq = properties.bandsCenterFreq

        binding.tvDbMax.text = (upperBandLevel / 100).toString() + " dB"
        binding.tvDbMin.text = (lowestBandLevel / 100).toString() + " dB"

        binding.tvBand1CenterFreq.text = bandsCenterFreq[0].formatAsFreq()
        binding.tvBand2CenterFreq.text = bandsCenterFreq[1].formatAsFreq()
        binding.tvBand3CenterFreq.text = bandsCenterFreq[2].formatAsFreq()
        binding.tvBand4CenterFreq.text = bandsCenterFreq[3].formatAsFreq()
        binding.tvBand5CenterFreq.text = bandsCenterFreq[4].formatAsFreq()

        binding.eqSwitch.setOnCheckedChangeListener(this)
        binding.virtualizerSwitch.setOnCheckedChangeListener(this)
        binding.bassSwitch.setOnCheckedChangeListener(this)
        binding.reverbSwitch.setOnCheckedChangeListener(this)

        binding.band1.apply {
            valueFrom = lowestBandLevel.toSliderValue()
            valueTo = upperBandLevel.toSliderValue()
            stepSize = STEP_SIZE
            addOnChangeListener(this@EqualizerFragment)
            addOnSliderTouchListener(this@EqualizerFragment)
        }
        binding.band2.apply {
            valueFrom = lowestBandLevel.toSliderValue()
            valueTo = upperBandLevel.toSliderValue()
            stepSize = STEP_SIZE
            addOnChangeListener(this@EqualizerFragment)
            addOnSliderTouchListener(this@EqualizerFragment)
        }
        binding.band3.apply {
            valueFrom = lowestBandLevel.toSliderValue()
            valueTo = upperBandLevel.toSliderValue()
            stepSize = STEP_SIZE
            addOnChangeListener(this@EqualizerFragment)
            addOnSliderTouchListener(this@EqualizerFragment)
        }
        binding.band4.apply {
            valueFrom = lowestBandLevel.toSliderValue()
            valueTo = upperBandLevel.toSliderValue()
            stepSize = STEP_SIZE
            addOnChangeListener(this@EqualizerFragment)
            addOnSliderTouchListener(this@EqualizerFragment)
        }
        binding.band5.apply {
            valueFrom = lowestBandLevel.toSliderValue()
            valueTo = upperBandLevel.toSliderValue()
            stepSize = STEP_SIZE
            addOnChangeListener(this@EqualizerFragment)
            addOnSliderTouchListener(this@EqualizerFragment)
        }
        if (properties.bassBoostSupport) {
            binding.bassBoostBand.apply {
                valueFrom = MIN_BASS_BOOST_VALUE
                valueTo = MAX_BASS_BOOST_VALUE
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
                valueFrom = MIN_VIRTUALIZER_VALUE
                valueTo = MAX_VIRTUALIZER_VALUE
                stepSize = STEP_SIZE
                addOnChangeListener(this@EqualizerFragment)
                addOnSliderTouchListener(this@EqualizerFragment)
            }
        } else {
            binding.eqSwitch.isEnabled = false
            binding.surroundBand.isEnabled = false
        }

        setupPresetChips()
        //setupReverbChips()
    }

    private fun setObservers() {
        effectController.eqState.getPresetNumber().observe(viewLifecycleOwner) {
            setSelectedPresetChip(it)
        }

        effectController.eqState.getIsEnabled().observe(viewLifecycleOwner) {
            binding.eqSwitch.isChecked = it
        }

        effectController.eqState.getBandValues().observe(viewLifecycleOwner) {
            binding.band1.value = it[0].toSliderValue()
            binding.band2.value = it[1].toSliderValue()
            binding.band3.value = it[2].toSliderValue()
            binding.band4.value = it[3].toSliderValue()
            binding.band5.value = it[4].toSliderValue()
        }

        effectController.bassBoostState.getBassStrength().observe(viewLifecycleOwner) {
            binding.bassBoostBand.value = it.toSliderValue()
        }

        effectController.bassBoostState.getIsEnabled().observe(viewLifecycleOwner) {
            binding.bassSwitch.isChecked = it
        }

        effectController.virtualizerState.getIsEnabled().observe(viewLifecycleOwner) {
            binding.virtualizerSwitch.isChecked = it
        }

        effectController.virtualizerState.getVirtualizerStrength().observe(viewLifecycleOwner) {
            binding.surroundBand.value = it.toSliderValue()
        }
    }

    private fun setSelectedPresetChip(presetNumber: Short) {
        binding.presetChipGroup.forEach {
            if ((it as Chip).tag == presetNumber) {
                //binding.hsChips.smoothScrollTo(it.x.roundToInt(), it.y.roundToInt())
                it.isChecked = true
                return@forEach
            }
        }
    }

    private fun setupPresetChips() {
        val presets = effectController.getProperties().presets

        binding.presetChipGroup.isSingleSelection = true

        Chip(requireContext()).apply {
            isCheckable = true
            tag = EqState.CUSTOM_PRESET
            text = "Custom"
            setOnCheckedChangeListener { compoundButton, isChecked ->
                if (isChecked)
                    onEqPresetChange(compoundButton.tag.toString().toShort())
            }
        }.also { newChip ->
            binding.presetChipGroup.addView(newChip)
        }

        for (preset in presets) {
            Chip(requireContext()).apply {
                isCheckable = true
                tag = preset.presetNumber
                text = preset.name
                setOnCheckedChangeListener { compoundButton, isChecked ->
                    if (isChecked)
                        onEqPresetChange(compoundButton.tag.toString().toShort())
                }
            }.also { newChip ->
                binding.presetChipGroup.addView(newChip)
            }
        }
    }

//    private fun setupReverbChips() {
//        for(reverbPreset in reverbPresetMap) {
//            Chip(requireContext()).apply {
//                isCheckable = true
//                text = reverbPreset.key
//                tag = reverbPreset.value
//                setOnCheckedChangeListener { compoundButton, isChecked ->
//                    if (isChecked)
//                        onReverbPresetChange(compoundButton.tag.toString().toShort())
//                }
//            }.also { newChip ->
//                binding.reverbChipGroup.addView(newChip)
//            }
//        }
//    }

    private fun onEqPresetChange(presetNumber: Short) {
        effectController.setPreset(presetNumber)
    }

//    private fun onReverbPresetChange(reverbPreset: Short) {
//        effectController.setReverbPreset(reverbPreset)
//    }

    private fun onSliderChangeValue(slider: Slider, value: Float, fromUser: Boolean) {
        if (!fromUser)
            return

        when (slider.id) {
            R.id.band1 -> {
                effectController.setBandLevel(0, value.toAdjustableValue())
            }
            R.id.band2 -> {
                effectController.setBandLevel(1, value.toAdjustableValue())
            }
            R.id.band3 -> {
                effectController.setBandLevel(2, value.toAdjustableValue())
            }
            R.id.band4 -> {
                effectController.setBandLevel(3, value.toAdjustableValue())
            }
            R.id.band5 -> {
                effectController.setBandLevel(4, value.toAdjustableValue())
            }
            R.id.bassBoostBand -> {
                effectController.setBassStrength(value.toAdjustableValue())
            }
            R.id.surroundBand -> {
                effectController.setVirtualizeStrength(value.toAdjustableValue())
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