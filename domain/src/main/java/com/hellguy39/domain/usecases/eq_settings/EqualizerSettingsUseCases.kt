package com.hellguy39.domain.usecases.eq_settings

class EqualizerSettingsUseCases(
    val getEqualizerSettings: GetEqualizerSettingsUseCase,
    val saveEqualizerSettingsUseCase: SaveEqualizerSettingsUseCase,
    val saveEqSwitchUseCase: SaveEqSwitchUseCase,
    val saveBassBoostSwitchUseCase: SaveBassBoostSwitchUseCase,
    val saveVirtualizerSwitchUseCase: SaveVirtualizerSwitchUseCase,
    val saveEqBandsLevelUseCase: SaveEqBandsLevelUseCase,
    val saveEqPresetUseCase: SaveEqPresetUseCase,
    val saveBassBoostValueUseCase: SaveBassBoostValueUseCase,
    val saveVirtualizerValueUseCase: SaveVirtualizerValueUseCase
)