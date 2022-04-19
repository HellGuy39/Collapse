package com.hellguy39.domain.repositories

import com.hellguy39.domain.models.EqualizerSettings

interface EqualizerSettingsRepository {

    fun getEqualizerSettings(): EqualizerSettings

    fun saveEqualizerSettings(equalizerSettings: EqualizerSettings)

    fun savePreset(preset: Int)

    fun saveEqSwitch(isEnabled: Boolean)

    fun saveBassBoostSwitch(isEnabled: Boolean)

    fun saveBassBoostValue(bass: Float)

    fun saveVirtualizerValue(virtualize: Float)

    fun saveVirtualizerSwitch(isEnabled: Boolean)

    fun saveBandsLevel(levels: List<Float>)
}