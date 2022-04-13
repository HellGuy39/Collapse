package com.hellguy39.domain.usecases.eq_settings

import com.hellguy39.domain.repositories.EqualizerSettingsRepository

class SaveBandsLevelEQSettingsUseCase(private val repository: EqualizerSettingsRepository) {
    operator fun invoke(levels: List<Short>) {
        val newLevels = mutableListOf<Float>()

        for (n in levels.indices) {
            newLevels.add(levels[n].toFloat())
        }

        repository.saveBandsLevel(levels = newLevels)
    }
}