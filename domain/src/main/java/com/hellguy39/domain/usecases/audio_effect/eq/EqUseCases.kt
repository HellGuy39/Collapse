package com.hellguy39.domain.usecases.audio_effect.eq

data class EqUseCases(
    val getCustomEqPresetUseCase: GetCustomEqPresetUseCase,
    val saveCustomEqPresetUseCase: SaveCustomEqPresetUseCase,
    val getEqPresetNumberUseCase: GetEqPresetNumberUseCase,
    val saveEqPresetNumberUseCase: SaveEqPresetNumberUseCase,
    val getIsEqEnabledUseCase: GetIsEqEnabledUseCase,
    val saveIsEqEnabledUseCase: SaveIsEqEnabledUseCase
)
