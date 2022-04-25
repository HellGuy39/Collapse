package com.hellguy39.domain.usecases.eq_settings

import com.hellguy39.domain.repositories.EqualizerSettingsRepository

class SaveBassBoostValueUseCase (private val repository: EqualizerSettingsRepository) {
    operator fun invoke(bass: Short) {
        repository.saveBassBoostValue(value = bass)
    }
}