package com.hellguy39.domain.repositories

import com.hellguy39.domain.models.EqualizerSettings

interface EqualizerSettingsRepository {

    fun getEqualizerSettings(): EqualizerSettings

    fun saveEqualizerSettings(equalizerSettings: EqualizerSettings)

}