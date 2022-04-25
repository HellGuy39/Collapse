package com.hellguy39.domain.usecases.eq_settings

import com.hellguy39.domain.repositories.EqualizerSettingsRepository

class SaveVirtualizerValueUseCase (private val repository: EqualizerSettingsRepository) {
    operator fun invoke(virtualize: Short) {
        repository.saveVirtualizerValue(value = virtualize)
    }
}