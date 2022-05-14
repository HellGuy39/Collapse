package com.hellguy39.domain.usecases.audio_effect.eq

import com.hellguy39.domain.repositories.EqualizerRepository

class GetIsEqEnabledUseCase(private val repository: EqualizerRepository) {
    operator fun invoke(): Boolean {
        return repository.getIsEnabled()
    }
}