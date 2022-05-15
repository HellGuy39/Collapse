package com.hellguy39.domain.usecases.audio_effect.virtualizer

import com.hellguy39.domain.repositories.VirtualizerRepository

class GetVirtualizerStrengthUseCase(private val repository: VirtualizerRepository) {
    operator fun invoke(): Short {
        return repository.getStrength()
    }
}