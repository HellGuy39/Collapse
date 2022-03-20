package com.hellguy39.collapse.presentaton.fragments.equalizer

import android.media.audiofx.AudioEffect
import android.media.audiofx.Equalizer
import android.media.audiofx.Virtualizer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.slider.Slider
import com.hellguy39.collapse.R
import com.hellguy39.collapse.databinding.EqualizerFragmentBinding
import com.hellguy39.collapse.presentaton.services.PlayerService

class EqualizerFragment : Fragment(R.layout.equalizer_fragment) {

    companion object {
        fun newInstance() = EqualizerFragment()
    }

    private lateinit var viewModel: EqualizerViewModel
    private lateinit var binding: EqualizerFragmentBinding
    private lateinit var equalizer: Equalizer
    private lateinit var virtualizer: Virtualizer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[EqualizerViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = EqualizerFragmentBinding.bind(view)
        setObservers()
    }

    private fun setObservers() {
        PlayerService.getAudioSessionId().observe(viewLifecycleOwner) {
            if (isCorrectId(it))
                initEQ(it)
        }
    }

    private fun initEQ(id: Int) {
        equalizer = Equalizer(0, id)
        virtualizer = Virtualizer(0, id)

        equalizer.enabled = true

        setupEqualizer()
    }

    private fun setupEqualizer() {

        val numberOfBands = equalizer.numberOfBands
        val lowestBandLevel = equalizer.bandLevelRange[0]
        val upperBandLevel = equalizer.bandLevelRange[1]

        val bandsCenterFreq = ArrayList<Int>(0)
        val bandsUpperFreq = ArrayList<Int>(0)
        val bandsLowerFreq = ArrayList<Int>(0)

        (0 until numberOfBands)
            .map { equalizer.getCenterFreq(it.toShort()) }
            .mapTo(bandsCenterFreq) { it / 1000 }

        (0 until numberOfBands)
            .map { equalizer.getBandFreqRange(it.toShort())[1] }
            .mapTo(bandsUpperFreq) { it / 1000 }

        (0 until numberOfBands)
            .map { equalizer.getBandFreqRange(it.toShort())[0] }
            .mapTo(bandsLowerFreq) { it / 1000 }

        val numberOfPresets = equalizer.numberOfPresets

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
        }.addOnChangeListener { slider, value, fromUser ->
            equalizer.setBandLevel(0, (value * 100).toInt().toShort())
        }
        binding.band2.apply {
            valueFrom = (lowestBandLevel / 100).toFloat()
            valueTo = (upperBandLevel / 100).toFloat()
        }.addOnChangeListener { slider, value, fromUser ->
            equalizer.setBandLevel(1, (value * 100).toInt().toShort())
        }
        binding.band3.apply {
            valueFrom = (lowestBandLevel / 100).toFloat()
            valueTo = (upperBandLevel / 100).toFloat()
        }.addOnChangeListener { slider, value, fromUser ->
            equalizer.setBandLevel(2, (value * 100).toInt().toShort())
        }
        binding.band4.apply {
            valueFrom = (lowestBandLevel / 100).toFloat()
            valueTo = (upperBandLevel / 100).toFloat()
        }.addOnChangeListener { slider, value, fromUser ->
            equalizer.setBandLevel(3, (value * 100).toInt().toShort())
        }
        binding.band5.apply {
            valueFrom = (lowestBandLevel / 100).toFloat()
            valueTo = (upperBandLevel / 100).toFloat()
        }.addOnChangeListener { slider, value, fromUser ->
            equalizer.setBandLevel(4, (value * 100).toInt().toShort())
        }

//        val settings = Equalizer.Settings()
//
//        settings.bandLevels = shortArrayOf(1499, 1499, 1499, 1499, 1499)
//        settings.numBands = numberOfBands
//
//        equalizer.properties = settings
//
//        equalizer.setBandLevel(0, 1499)
//        equalizer.setBandLevel(1, 1499)
//        equalizer.setBandLevel(2, 1499)
//        equalizer.setBandLevel(3, 1499)
//        equalizer.setBandLevel(4, 1499)
    }

    private fun isCorrectId(id: Int?): Boolean = (id != null || id != 0)
}