package com.hellguy39.domain.usecases.eq_settings

import com.hellguy39.domain.repositories.EqualizerSettingsRepository

class SavePresetEQSettingsUseCase(private val equalizerSettingsRepository: EqualizerSettingsRepository) {
    operator fun invoke(preset: Int) {
        equalizerSettingsRepository.savePreset(preset = preset)
    }
}