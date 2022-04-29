package com.hellguy39.domain.usecases.eq_settings

import com.hellguy39.domain.repositories.EqualizerSettingsRepository

class SaveReverbPresetUseCase(private val repository: EqualizerSettingsRepository) {
    operator fun invoke(preset: Short) {
        repository.saveReverbPreset(preset)
    }
}