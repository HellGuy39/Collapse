package com.hellguy39.domain.usecases.audio_effect.bass_boost

data class BassBoostUseCases(
    val getBassBoostStrengthUseCase: GetBassBoostStrengthUseCase,
    val getIsBassBoostEnabledUseCase: GetIsBassBoostEnabledUseCase,
    val saveBassBoostStrengthUseCase: SaveBassBoostStrengthUseCase,
    val saveIsBassBoostEnabledUseCase: SaveIsBassBoostEnabledUseCase
)