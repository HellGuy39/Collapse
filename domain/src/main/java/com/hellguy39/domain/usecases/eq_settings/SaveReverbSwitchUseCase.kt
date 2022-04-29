package com.hellguy39.domain.usecases.eq_settings

import com.hellguy39.domain.repositories.EqualizerSettingsRepository

class SaveReverbSwitchUseCase(private val repository: EqualizerSettingsRepository) {
    operator fun invoke(enabled: Boolean) {
        repository.saveReverbSwitch(enabled)
    }
}