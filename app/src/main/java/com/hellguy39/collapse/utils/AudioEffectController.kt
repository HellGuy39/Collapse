package com.hellguy39.collapse.utils

import android.media.audiofx.BassBoost
import android.media.audiofx.Equalizer
import android.media.audiofx.Virtualizer
import android.util.Log
import com.hellguy39.domain.models.EqualizerProperties
import com.hellguy39.domain.models.EqualizerSettings
import com.hellguy39.domain.usecases.GetEqualizerPropertiesUseCase
import com.hellguy39.domain.usecases.eq_settings.EqualizerSettingsUseCases

class AudioEffectController(
    private val getEqualizerPropertiesUseCase: GetEqualizerPropertiesUseCase,
    private val equalizerSettingsUseCases: EqualizerSettingsUseCases
) {
    private val equalizerProperties: EqualizerProperties = getEqualizerPropertiesUseCase.invoke()
    private val equalizerSettings: EqualizerSettings = equalizerSettingsUseCases.getEqualizerSettingsUseCase.invoke()

    private var virtualizer: Virtualizer? = null
    private var bassBoost: BassBoost? = null
    private var eq: Equalizer? = null

    fun getProperties(): EqualizerProperties = equalizerProperties

    fun getCurrentEqualizerSettings(): EqualizerSettings = equalizerSettings

    fun init(audioSessionId: Int) {
        virtualizer = Virtualizer(1000, audioSessionId)
        eq = Equalizer(1000, audioSessionId)
        bassBoost = BassBoost(1000, audioSessionId)

        applySettings()
    }

    private fun applySettings() {

        val settings = equalizerSettingsUseCases.getEqualizerSettingsUseCase.invoke()

        setEqEnabled(settings.isEqEnabled)

        if(settings.preset == (-1).toShort()) {
            setBandLevel(0, settings.band1Level)
            setBandLevel(1, settings.band2Level)
            setBandLevel(2, settings.band3Level)
            setBandLevel(3, settings.band4Level)
            setBandLevel(4, settings.band5Level)
        } else {
            setPreset(settings.preset)
        }

        if(equalizerProperties.virtualizerSupport) {
            setVirtualizeEnabled(settings.isVirtualizerEnabled)
            setVirtualizeStrength(settings.bandVirtualizer)
        }

        if(equalizerProperties.bassBoostSupport) {
            setBassEnabled(settings.isBassEnabled)
            setBassStrength(settings.bandVirtualizer)
        }
    }

    //***** Equalizer

    fun setEqEnabled(enabled: Boolean) {
        saveEqSwitch(enabled)
        eq?.enabled = enabled
    }

    fun setBandLevel(band: Short, level: Short) {
        saveBandLevel(band, level)
        eq?.setBandLevel(band, level)
        when(band) {
            (0).toShort() -> equalizerSettings.band1Level = level
            (1).toShort() -> equalizerSettings.band2Level = level
            (2).toShort() -> equalizerSettings.band3Level = level
            (3).toShort() -> equalizerSettings.band4Level = level
            (4).toShort() -> equalizerSettings.band5Level = level
        }
    }

    fun setPreset(preset: Short) {
        savePreset(preset)
        eq?.usePreset(preset)
        equalizerSettings.preset = preset
    }

    //***** BassBoost

    fun setBassStrength(value: Short) {
        saveBassBoostValue(value)
        bassBoost?.setStrength(value)
        equalizerSettings.bandBassBoost = value
    }

    fun setBassEnabled(enabled: Boolean) {
        saveBassBoostSwitch(enabled)
        bassBoost?.enabled = enabled
        equalizerSettings.isBassEnabled = enabled
    }

    //***** Virtualize

    fun setVirtualizeStrength(value: Short) {
        saveVirtualizerValue(value)
        virtualizer?.setStrength(value)
        equalizerSettings.bandVirtualizer = value
    }

    fun setVirtualizeEnabled(enabled: Boolean) {
        saveVirtualizerSwitch(enabled)
        virtualizer?.enabled = enabled
        equalizerSettings.isVirtualizerEnabled = enabled
    }

    //***** Save options

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