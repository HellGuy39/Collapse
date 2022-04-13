package com.hellguy39.domain.usecases.eq_settings

class EqualizerSettingsUseCases(
    val getEqualizerSettings: GetEqualizerSettingsUseCase,
    val saveEqualizerSettingsUseCase: SaveEqualizerSettingsUseCase,
    val savePresetEqualizerSettingsUseCase: SavePresetEQSettingsUseCase,
    val saveBandsLevelEQSettingsUseCase: SaveBandsLevelEQSettingsUseCase,
    val saveBassBoostEQSettingsUseCase: SaveBassBoostEQSettingsUseCase,
    val saveVirtualizerEQSettingsUseCase: SaveVirtualizerEQSettingsUseCase,
    val saveIsEnabledEQSettingsUseCase: SaveIsEnabledEQSettingsUseCase
)