package com.hellguy39.collapse.utils

import android.media.audiofx.BassBoost
import android.media.audiofx.Equalizer
import android.media.audiofx.PresetReverb
import android.media.audiofx.Virtualizer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hellguy39.domain.models.EqualizerPreset
import com.hellguy39.domain.models.EqualizerProperties
import com.hellguy39.domain.models.EqualizerSettings
import com.hellguy39.domain.usecases.GetEqualizerPropertiesUseCase
import com.hellguy39.domain.usecases.eq_settings.EqualizerSettingsUseCases

class AudioEffectController(
    private val getEqualizerPropertiesUseCase: GetEqualizerPropertiesUseCase,
    private val equalizerSettingsUseCases: EqualizerSettingsUseCases
) {

    companion object {
        const val NONE_PRESET: Short = -1
        //const val CUSTOM_PRESET: Short = -2
    }

    private val equalizerProperties: EqualizerProperties = getEqualizerPropertiesUseCase.invoke()
    private val equalizerSettings = MutableLiveData<EqualizerSettings>(
        equalizerSettingsUseCases.getEqualizerSettingsUseCase.invoke()
    )

    private var virtualizer: Virtualizer? = null
    private var bassBoost: BassBoost? = null
    private var eq: Equalizer? = null
    private var reverb: PresetReverb? = null

    private val reverbPresetList = listOf(
        PresetReverb.PRESET_NONE,
        PresetReverb.PRESET_SMALLROOM,
        PresetReverb.PRESET_MEDIUMROOM,
        PresetReverb.PRESET_LARGEROOM,
        PresetReverb.PRESET_MEDIUMHALL,
        PresetReverb.PRESET_LARGEHALL,
        PresetReverb.PRESET_PLATE
    )

    fun getReverbPresetList(): List<Short> = reverbPresetList

    fun getProperties(): EqualizerProperties = equalizerProperties

    fun getCurrentSettings(): LiveData<EqualizerSettings> = equalizerSettings

    fun init(audioSessionId: Int) {
        virtualizer = Virtualizer(1000, audioSessionId)
        eq = Equalizer(1000, audioSessionId)
        bassBoost = BassBoost(1000, audioSessionId)
        reverb = PresetReverb(1000, audioSessionId)

        applySettings()
    }

    private fun applySettings() {

        val settings = equalizerSettingsUseCases.getEqualizerSettingsUseCase.invoke()

        setEqEnabled(settings.isEqEnabled)

        when(settings.preset) {
            else -> {

            }
        }

        if(equalizerProperties.virtualizerSupport) {
            setVirtualizeEnabled(settings.isVirtualizerEnabled)
            setVirtualizeStrength(settings.bandVirtualizer)
        }

        if(equalizerProperties.bassBoostSupport) {
            setBassEnabled(settings.isBassEnabled)
            setBassStrength(settings.bandVirtualizer)
        }

        reverb?.preset = settings.reverbPreset
    }

    //***** Equalizer

    fun setEqEnabled(enabled: Boolean) {
        saveEqSwitch(enabled)
        eq?.enabled = enabled
        equalizerSettings.value?.isEqEnabled = enabled
    }

    fun setBandLevel(band: Short, level: Short) {
        saveBandLevel(band, level)
        eq?.setBandLevel(band, level)
        when(band) {
            (0).toShort() -> equalizerSettings.value?.band1Level = level
            (1).toShort() -> equalizerSettings.value?.band2Level = level
            (2).toShort() -> equalizerSettings.value?.band3Level = level
            (3).toShort() -> equalizerSettings.value?.band4Level = level
            (4).toShort() -> equalizerSettings.value?.band5Level = level
        }
    }

    fun setPreset(inputPresetNumber: Short) {
        savePreset(inputPresetNumber)
        when(inputPresetNumber) {
            NONE_PRESET -> {

            }
//            CUSTOM_PRESET -> {
//
//            }
            else -> {
                eq?.usePreset(inputPresetNumber)
                equalizerSettings.value?.let {
                    for (preset in equalizerProperties.presets) {
                        if (preset.presetNumber == inputPresetNumber) {
                            it.band1Level = preset.band1Level
                            it.band2Level = preset.band2Level
                            it.band3Level = preset.band3Level
                            it.band4Level = preset.band4Level
                            it.band5Level = preset.band5Level
                            it.preset = preset.presetNumber
                        }
                    }
                }
            }
        }
    }

    //***** BassBoost

    fun setBassStrength(value: Short) {
        saveBassBoostValue(value)
        bassBoost?.setStrength(value)
        equalizerSettings.value?.bandBassBoost = value
    }

    fun setBassEnabled(enabled: Boolean) {
        saveBassBoostSwitch(enabled)
        bassBoost?.enabled = enabled
        equalizerSettings.value?.isBassEnabled = enabled
    }

    //***** Virtualize

    fun setVirtualizeStrength(value: Short) {
        saveVirtualizerValue(value)
        virtualizer?.setStrength(value)
        equalizerSettings.value?.bandVirtualizer = value
    }

    fun setVirtualizeEnabled(enabled: Boolean) {
        saveVirtualizerSwitch(enabled)
        virtualizer?.enabled = enabled
        equalizerSettings.value?.isVirtualizerEnabled = enabled
    }

    //***** Reverb

    fun setReverbPreset(preset: Short) {
        saveReverbPreset(preset)
        reverb?.preset = preset
        equalizerSettings.value?.reverbPreset = preset
    }

    fun setReverbEnabled(enabled: Boolean) {
        saveReverbSwitch(enabled)
        reverb?.enabled = enabled
        equalizerSettings.value?.isReverbEnabled = enabled
    }

    //***** Save options

    private fun saveReverbSwitch(enabled: Boolean) {
        equalizerSettingsUseCases.saveReverbSwitchUseCase.invoke(enabled)
    }

    private fun saveReverbPreset(preset: Short) {
        equalizerSettingsUseCases.saveReverbPresetUseCase.invoke(preset)
    }

    private fun saveVirtualizerSwitch(isEnabled: Boolean) {
        equalizerSettingsUseCases.saveVirtualizerSwitchUseCase.invoke(isEnabled = isEnabled)
    }

    private fun saveVirtualizerValue(virtualize: Short) {
        equalizerSettingsUseCases.saveVirtualizerValueUseCase.invoke(virtualize = virtualize)
    }

    private fun saveBassBoostSwitch(isEnabled: Boolean) {
        equalizerSettingsUseCases.saveBassBoostSwitchUseCase.invoke(isEnabled = isEnabled)
    }

    private fun saveBassBoostValue(bass: Short) {
        equalizerSettingsUseCases.saveBassBoostValueUseCase.invoke(bass = bass)
    }

    private fun savePreset(preset: Short) {
        equalizerSettingsUseCases.saveEqPresetUseCase.invoke(preset = preset)
    }

    private fun saveEqSwitch(isEnabled: Boolean) {
        equalizerSettingsUseCases.saveEqSwitchUseCase.invoke(isEnabled = isEnabled)
    }

    private fun saveBandsLevel(levels: List<Short>) {
        equalizerSettingsUseCases.saveEqBandsLevelUseCase.invoke(levels = levels)
    }

    private fun saveBandLevel(band: Short, level: Short) {
        equalizerSettingsUseCases.saveBandLevelUseCase.invoke(band, level)
    }

}