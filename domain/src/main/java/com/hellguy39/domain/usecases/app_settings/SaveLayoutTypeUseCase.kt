package com.hellguy39.domain.usecases.app_settings

import com.hellguy39.domain.repositories.AppSettingsRepository
import com.hellguy39.domain.utils.LayoutType

class SaveLayoutTypeUseCase(private val repository: AppSettingsRepository) {
    operator fun invoke(type: LayoutType) {
        repository.saveLayoutType(type)
    }
}