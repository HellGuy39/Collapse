package com.hellguy39.domain.usecases.eq_settings

import com.hellguy39.domain.models.EqualizerSettings
import com.hellguy39.domain.repositories.EqualizerSettingsRepository

class SaveEqualizerSettingsUseCase(private val repository: EqualizerSettingsRepository) {
    operator fun invoke(equalizerSettings: EqualizerSettings) {
        repository.saveEqualizerSettings(equalizerSettings)
    }
}