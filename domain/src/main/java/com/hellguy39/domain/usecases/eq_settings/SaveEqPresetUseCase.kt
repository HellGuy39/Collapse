package com.hellguy39.domain.usecases.eq_settings

import com.hellguy39.domain.repositories.EqualizerSettingsRepository

class SaveEqPresetUseCase(private val equalizerSettingsRepository: EqualizerSettingsRepository) {
    operator fun invoke(preset: Short) {
        equalizerSettingsRepository.savePreset(preset = preset)
    }
}