package com.hellguy39.domain.usecases.eq_settings

import com.hellguy39.domain.repositories.EqualizerSettingsRepository

class SaveEqSwitchUseCase (private val repository: EqualizerSettingsRepository) {
    operator fun invoke(isEnabled: Boolean) {
        repository.saveEqSwitch(isEnabled)
    }
}