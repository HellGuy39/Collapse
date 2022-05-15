package com.hellguy39.domain.usecases.app_settings

import com.hellguy39.domain.models.AppSettings
import com.hellguy39.domain.repositories.AppSettingsRepository

class GetAppSettingsUseCase(private val repository: AppSettingsRepository) {
    operator fun invoke(): AppSettings {
        return repository.getSettings()
    }
}