package com.hellguy39.domain.usecases.audio_effect.virtualizer

import com.hellguy39.domain.repositories.VirtualizerRepository

class GetIsVirtualizerEnabledUseCase(private val repository: VirtualizerRepository) {
    operator fun invoke(): Boolean {
        return repository.getIsEnabled()
    }
}