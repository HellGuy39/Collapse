package com.hellguy39.domain.usecases.audio_effect.virtualizer

data class VirtualizerUseCases(
    val getIsVirtualizerEnabledUseCase: GetIsVirtualizerEnabledUseCase,
    val getVirtualizerStrengthUseCase: GetVirtualizerStrengthUseCase,
    val saveIsVirtualizerEnabledUseCase: SaveIsVirtualizerEnabledUseCase,
    val saveVirtualizerStrengthUseCase: SaveVirtualizerStrengthUseCase
)