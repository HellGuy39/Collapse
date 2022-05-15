package com.hellguy39.domain.usecases.app_settings

data class AppSettingsUseCases(
    val getAppSettingsUseCase: GetAppSettingsUseCase,
    val saveIsAnimationsEnabledUseCase: SaveIsAnimationsEnabledUseCase,
    val saveIsSaveStateEnabledUseCase: SaveIsSaveStateEnabledUseCase
)
