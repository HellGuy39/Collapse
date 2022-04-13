package com.hellguy39.domain.repositories

import com.hellguy39.domain.models.EqualizerSettings

interface EqualizerSettingsRepository {

    fun getEqualizerSettings(): EqualizerSettings

    fun saveEqualizerSettings(equalizerSettings: EqualizerSettings)

    fun savePreset(preset: Int)

    fun saveIsEnabled(isEnabled: Boolean)

    fun saveBassBoostValue(bass: Float)

    fun saveVirtualizer(virtualize: Float)

    fun saveBandsLevel(levels: List<Float>)
}