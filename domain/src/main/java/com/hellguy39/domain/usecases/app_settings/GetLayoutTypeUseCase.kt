package com.hellguy39.domain.usecases.app_settings

import com.hellguy39.domain.repositories.AppSettingsRepository
import com.hellguy39.domain.utils.LayoutType

class GetLayoutTypeUseCase(private val repository: AppSettingsRepository) {
    operator fun invoke(): LayoutType {
        return repository.getLayoutType()
    }
}