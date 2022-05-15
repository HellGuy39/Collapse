package com.hellguy39.collapse.controllers.audio_effect

import android.media.audiofx.BassBoost
import android.media.audiofx.Equalizer
import android.media.audiofx.PresetReverb
import android.media.audiofx.Virtualizer
import android.util.Log
import com.hellguy39.collapse.presentaton.services.PlayerService
import com.hellguy39.domain.models.EqualizerProperties
import com.hellguy39.domain.usecases.GetEqualizerPropertiesUseCase

class AudioEffectController(
    private val getEqualizerPropertiesUseCase: GetEqualizerPropertiesUseCase,
    val eqState: EqState,
    val bassBoostState: BassBoostState,
    val virtualizerState: VirtualizerState,
    val reverbState: ReverbState
) {

    private val equalizerProperties: EqualizerProperties = getEqualizerPropertiesUseCase.invoke()
    private var virtualizer: Virtualizer? = null
    private var bassBoost: BassBoost? = null
    private var eq: Equalizer? = null
    private var reverb: PresetReverb? = null
//
//    private val reverbPresetList = listOf(
//        PresetReverb.PRESET_NONE,
//        PresetReverb.PRESET_SMALLROOM,
//        PresetReverb.PRESET_MEDIUMROOM,
//        PresetReverb.PRESET_LARGEROOM,
//        PresetReverb.PRESET_MEDIUMHALL,
//        PresetReverb.PRESET_LARGEHALL,
//        PresetReverb.PRESET_PLATE
//    )

//    fun getReverbPresetList(): List<Short> = reverbPresetList

    fun getProperties(): EqualizerProperties = equalizerProperties

    init {
        eqState.loadSavedState(presets = equalizerProperties.presets)
        bassBoostState.loadSavedState()
        virtualizerState.loadSavedState()
        //reverbState.loadSavedState()
    }

    private fun applyValuesToEq(values: MutableList<Short>) {
        eq?.let { eq ->
            for (n in values.indices) {
                eq.setBandLevel(n.toShort(), values[n])
            }
        }
    }

    fun updateAudioSession(audioSessionId: Int) {

        eq = Equalizer(1000, audioSessionId)
        //reverb = PresetReverb(1000, audioSessionId)

        if (equalizerProperties.virtualizerSupport) {
            virtualizer = Virtualizer(1000, audioSessionId)
            applyVirtualizerValues()
        }

        if (equalizerProperties.bassBoostSupport) {
            bassBoost = BassBoost(1000, audioSessionId)
            applyBassBoostValues()
        }

        applyEqValues()
        //applyReverbValues()
    }

    private fun applyEqValues() {
        eqState.getBandValues().value?.let {
            applyValuesToEq(it)
        }
        eqState.getIsEnabled().value?.let {
            eq?.enabled = it
        }
    }

    private fun applyBassBoostValues() {
        bassBoostState.getIsEnabled().value?.let {
            bassBoost?.enabled = it
        }
        bassBoostState.getBassStrength().value?.let {
            bassBoost?.setStrength(it)
        }
    }

    private fun applyVirtualizerValues() {
        virtualizerState.getIsEnabled().value?.let {
            virtualizer?.enabled = it
        }
        virtualizerState.getVirtualizerStrength().value?.let {
            virtualizer?.setStrength(it)
        }
    }

    private fun applyReverbValues() {

    }

    fun setEqEnabled(enabled: Boolean) {
        eq?.enabled = enabled
        eqState.updateIsEnabled(enabled)
    }

    fun setBandLevel(band: Short, level: Short) {
        eq?.setBandLevel(band, level)
        eqState.updateBandValue(band, level)
    }

    fun setPreset(presetNumber: Short) {
        when(presetNumber) {
            EqState.CUSTOM_PRESET -> eqState.getCustomPreset().value ?: mutableListOf<Short>(0,0,0,0,0)
            else -> getProperties().presets[presetNumber.toInt()].bandValues
        }.also { values ->
            applyValuesToEq(values)
            eqState.updatePreset(presetNumber, values)
        }
    }

    //***** BassBoost

    fun setBassStrength(value: Short) {
        bassBoost?.setStrength(value)
        bassBoostState.updateBassStrength(value)
    }

    fun setBassEnabled(enabled: Boolean) {
        bassBoost?.enabled = enabled
        bassBoostState.updateIsEnabled(enabled)
    }

    //***** Virtualizer

    fun setVirtualizeStrength(value: Short) {
        virtualizer?.setStrength(value)
        virtualizerState.updateVirtualizerStrength(value)
    }

    fun setVirtualizeEnabled(enabled: Boolean) {
        virtualizer?.enabled = enabled
        virtualizerState.updateIsEnabled(enabled)
    }

    //***** Reverb

    fun setReverbPreset(preset: Short) {
        //reverb?.preset = preset
//        reverb?.let {
//            PlayerService.setAuxEffect(it.id)
//        }
    }

    fun setReverbEnabled(enabled: Boolean) {
        //reverb?.enabled = enabled
    }
}