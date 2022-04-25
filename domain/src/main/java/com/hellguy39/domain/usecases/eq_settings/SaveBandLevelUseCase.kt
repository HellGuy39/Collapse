package com.hellguy39.domain.usecases.eq_settings

import com.hellguy39.domain.repositories.EqualizerSettingsRepository

class SaveBandLevelUseCase (private val repository: EqualizerSettingsRepository) {
    operator fun invoke(band: Short, level: Short) {
        repository.saveBandLevel(band, level)
    }
}