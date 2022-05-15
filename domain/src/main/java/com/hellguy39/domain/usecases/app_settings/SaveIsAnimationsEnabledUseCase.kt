package com.hellguy39.domain.usecases.app_settings

import com.hellguy39.domain.repositories.AppSettingsRepository

class SaveIsAnimationsEnabledUseCase(private val repository: AppSettingsRepository) {
    operator fun invoke(enabled: Boolean) {
        repository.saveIsAnimationsEnabled(enabled)
    }
}