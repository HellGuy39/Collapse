package com.hellguy39.domain.usecases.audio_effect.virtualizer

import com.hellguy39.domain.repositories.VirtualizerRepository

class SaveVirtualizerStrengthUseCase(private val repository: VirtualizerRepository) {
    operator fun invoke(strength: Short) {
        repository.saveStrength(strength)
    }
}