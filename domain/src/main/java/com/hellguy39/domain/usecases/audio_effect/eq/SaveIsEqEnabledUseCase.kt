package com.hellguy39.domain.usecases.audio_effect.eq

import com.hellguy39.domain.repositories.EqualizerRepository

class SaveIsEqEnabledUseCase(private val repository: EqualizerRepository) {
    operator fun invoke(enabled: Boolean) {
        repository.saveIsEnabled(enabled)
    }
}