package com.hellguy39.domain.usecases.audio_effect.eq

import com.hellguy39.domain.repositories.EqualizerRepository

class GetEqPresetNumberUseCase(private val repository: EqualizerRepository) {
    operator fun invoke(): Short {
        return repository.getPresetNumber()
    }
}