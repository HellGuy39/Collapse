package com.hellguy39.collapse.presentaton.fragments.equalizer

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.slider.Slider
import com.google.android.material.transition.platform.MaterialFadeThrough
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.EqualizerFragmentBinding
import com.hellguy39.collapse.presentaton.services.PlayerService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EqualizerFragment : Fragment(R.layout.equalizer_fragment),
    Slider.OnChangeListener,
    Slider.OnSliderTouchListener,
    CompoundButton.OnCheckedChangeListener
{

    companion object {
        fun newInstance() = EqualizerFragment()
        private const val STEP_SIZE = 1f
    }

    private lateinit var viewModel: EqualizerViewModel
    private lateinit var binding: EqualizerFragmentBinding

    private var selectedPreset = 0
    private var isPresetMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialFadeThrough()
        viewModel = ViewModelProvider(this)[EqualizerViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = EqualizerFragmentBinding.bind(view)

        enableUI(false)

        setObservers()
    }

    private fun setObservers() {
        PlayerService.isEqualizerInitialized().observe(viewLifecycleOwner) {
            if (it) {
                enableUI(true)

                setupEqualizer()
                setupPreset()
                setupBassBoost()
                setupVirtualizer()

                applySettings()

                binding.eqSwitch.setOnCheckedChangeListener(this)
                binding.virtualizerSwitch.setOnCheckedChangeListener(this)
                binding.bassSwitch.setOnCheckedChangeListener(this)
            }
        }
    }

    private fun enableUI(b: Boolean) {
        binding.eqSwitch.isEnabled = b
        binding.band1.isEnabled = b
        binding.band2.isEnabled = b
        binding.band3.isEnabled = b
        binding.band4.isEnabled = b
        binding.band5.isEnabled = b
        binding.bassBoostBand.isEnabled = b
        binding.surroundBand.isEnabled = b
        binding.acPreset.isEnabled = b
        binding.bassSwitch.isEnabled = b
        binding.virtualizerSwitch.isEnabled = b

        if (b)
            binding.cardEqMessage.visibility = View.GONE
        else
            binding.cardEqMessage.visibility = View.VISIBLE
    }

    private fun setupEqualizer() {

        val upperBandLevel = PlayerService.getUpperBandLevel()
        val lowestBandLevel = PlayerService.getLowestBandLevel()
        val bandsCenterFreq = PlayerService.getBandsCenterFreq()

        binding.tvDbMax.text = (upperBandLevel / 100).toString() + " dB"
        binding.tvDbMin.text = (lowestBandLevel / 100).toString() + " dB"

        binding.tvBand1CenterFreq.text = bandsCenterFreq[0].toString() + " Hz"
        binding.tvBand2CenterFreq.text = bandsCenterFreq[1].toString() + " Hz"
        binding.tvBand3CenterFreq.text = bandsCenterFreq[2].toString() + " Hz"
        binding.tvBand4CenterFreq.text = bandsCenterFreq[3].toString() + " Hz"
        binding.tvBand5CenterFreq.text = bandsCenterFreq[4].toString() + " Hz"

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
    }

    private fun applySettings() {
        val settings = viewModel.getEqualizerSettings()

        binding.eqSwitch.isChecked = settings.isEqEnabled
        binding.bassSwitch.isChecked = settings.isBassEnabled
        binding.virtualizerSwitch.isChecked = settings.isVirtualizerEnabled

        if (settings.preset == -1) {
            isPresetMode = false
            binding.band1.value = settings.band1Level / 100
            binding.band2.value = settings.band2Level / 100
            binding.band3.value = settings.band3Level / 100
            binding.band4.value = settings.band4Level / 100
            binding.band5.value = settings.band5Level / 100
            binding.acPreset.setText(binding.acPreset.adapter.getItem(PlayerService.getPresetNames().lastIndex).toString(), false)
        } else {
            isPresetMode = true
            selectedPreset = settings.preset
            binding.acPreset.setText(binding.acPreset.adapter.getItem(settings.preset).toString(), false)
            updateBands(PlayerService.getBandsLevels())
        }

        if (PlayerService.isBassBoostSupported())
            binding.bassBoostBand.value = settings.bandBassBoost / 100

        if (PlayerService.isVirtualizerSupported())
            binding.surroundBand.value = settings.bandVirtualizer / 100
    }

    private fun setupPreset() {
        val presets = PlayerService.getPresetNames()
        val presetAdapter = ArrayAdapter(requireContext(), R.layout.list_item, presets)
        binding.acPreset.setAdapter(presetAdapter)

        binding.acPreset.setOnItemClickListener { adapterView, view, i, l ->
            if (binding.acPreset.text.toString() == "Custom") {
                isPresetMode = false
                selectedPreset = -1
            } else {
                isPresetMode = true
                selectedPreset = i
            }

            viewModel.savePreset(selectedPreset)

            if (selectedPreset == -1) {
                applyCustomPreset()
            } else {
                PlayerService.usePreset(i.toShort())
                updateBands(PlayerService.getBandsLevels())
            }
        }
    }

    private fun applyCustomPreset() {
        val settings = viewModel.getEqualizerSettings()

        binding.band1.value = settings.band1Level / 100
        binding.band2.value = settings.band2Level / 100
        binding.band3.value = settings.band3Level / 100
        binding.band4.value = settings.band4Level / 100
        binding.band5.value = settings.band5Level / 100
    }

    private fun updateBands(list: List<Short>) {
        binding.band1.value = (list[0] / 100).toFloat()
        binding.band2.value = (list[1] / 100).toFloat()
        binding.band3.value = (list[2] / 100).toFloat()
        binding.band4.value = (list[3] / 100).toFloat()
        binding.band5.value = (list[4] / 100).toFloat()
    }

    private fun setupBassBoost() {
        if (PlayerService.isBassBoostSupported()) {
            binding.bassBoostBand.apply {
                valueFrom = 0f
                valueTo = 10f
                stepSize = STEP_SIZE
                addOnChangeListener(this@EqualizerFragment)
                addOnSliderTouchListener(this@EqualizerFragment)
            }
        } else {
            binding.bassBoostBand.isEnabled = false
        }
    }

    private fun setupVirtualizer() {
        if (PlayerService.isVirtualizerSupported()) {
            binding.surroundBand.apply {
                valueFrom = 0f
                valueTo = 10f
                stepSize = STEP_SIZE
                addOnChangeListener(this@EqualizerFragment)
                addOnSliderTouchListener(this@EqualizerFragment)
            }
        } else {
            binding.surroundBand.isEnabled = false
        }
    }

    private fun setupUItoCustomPreset() {
        isPresetMode = false
        binding.acPreset.setText(binding.acPreset.adapter.getItem(
            PlayerService.getPresetNames().lastIndex).toString(),
            false
        )
        viewModel.saveBandsLevel(PlayerService.getBandsLevels())
        viewModel.savePreset(-1)
    }

    private fun onSliderChangeValue(slider: Slider, value: Float, fromUser: Boolean) {
        when (slider.id) {
            R.id.band1 -> {
                if (fromUser)
                    setupUItoCustomPreset()
                PlayerService.setBandLevel(0, (value * 100).toInt().toShort())
            }
            R.id.band2 -> {
                if (fromUser)
                    setupUItoCustomPreset()
                PlayerService.setBandLevel(1, (value * 100).toInt().toShort())
            }
            R.id.band3 -> {
                if (fromUser)
                    setupUItoCustomPreset()
                PlayerService.setBandLevel(2, (value * 100).toInt().toShort())
            }
            R.id.band4 -> {
                if (fromUser)
                    setupUItoCustomPreset()
                PlayerService.setBandLevel(3, (value * 100).toInt().toShort())
            }
            R.id.band5 -> {
                if (fromUser)
                    setupUItoCustomPreset()
                PlayerService.setBandLevel(4, (value * 100).toInt().toShort())
            }
            R.id.bassBoostBand -> {
                PlayerService.setBassBoostBandLevel((value * 100).toInt().toShort())
                viewModel.saveBassBoostValue(value * 100)
            }
            R.id.surroundBand -> {
                PlayerService.setVirtualizerBandLevel((value * 100).toInt().toShort())
                viewModel.saveVirtualizerValue(value * 100)
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
            binding.eqSwitch.id -> {
                PlayerService.enableEq(b)
                viewModel.saveEqSwitch(b)
            }
            binding.bassSwitch.id -> {
                PlayerService.enableBass(b)
                viewModel.saveBassBoostSwitch(b)
            }
            binding.virtualizerSwitch.id -> {
                PlayerService.enableVirtualizer(b)
                viewModel.saveVirtualizerSwitch(b)
            }
        }
    }
}