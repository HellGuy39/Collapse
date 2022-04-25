package com.hellguy39.domain.usecases.eq_settings

import com.hellguy39.domain.repositories.EqualizerSettingsRepository

class SaveEqBandsLevelUseCase(private val repository: EqualizerSettingsRepository) {
    operator fun invoke(levels: List<Short>) {
        repository.saveBandsLevel(levels = levels)
    }
}