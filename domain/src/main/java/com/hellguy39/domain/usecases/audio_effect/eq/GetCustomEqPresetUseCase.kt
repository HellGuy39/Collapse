package com.hellguy39.domain.usecases.audio_effect.eq

import com.hellguy39.domain.repositories.EqualizerRepository

class GetCustomEqPresetUseCase(private val repository: EqualizerRepository) {
    operator fun invoke(): MutableList<Short> {
        return repository.getPreset()
    }
}