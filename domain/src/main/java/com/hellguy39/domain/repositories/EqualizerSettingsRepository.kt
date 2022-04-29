package com.hellguy39.domain.repositories

import com.hellguy39.domain.models.EqualizerSettings

interface EqualizerSettingsRepository {

    fun getEqualizerSettings(): EqualizerSettings

    fun saveEqualizerSettings(equalizerSettings: EqualizerSettings)

    fun savePreset(preset: Short)

    fun saveReverbPreset(preset: Short)

    fun saveReverbSwitch(isEnabled: Boolean)

    fun saveEqSwitch(isEnabled: Boolean)

    fun saveBassBoostSwitch(isEnabled: Boolean)

    fun saveBassBoostValue(value: Short)

    fun saveVirtualizerValue(value: Short)

    fun saveVirtualizerSwitch(isEnabled: Boolean)

    fun saveBandsLevel(levels: List<Short>)

    fun saveBandLevel(band: Short, level: Short)
}