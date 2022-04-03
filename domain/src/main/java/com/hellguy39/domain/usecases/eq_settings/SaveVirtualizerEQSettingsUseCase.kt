package com.hellguy39.domain.usecases.eq_settings

import com.hellguy39.domain.repositories.EqualizerSettingsRepository

class SaveVirtualizerEQSettingsUseCase (private val repository: EqualizerSettingsRepository) {
    operator fun invoke(virtualize: Float) {
        repository.saveVirtualizer(virtualize = virtualize)
    }
}