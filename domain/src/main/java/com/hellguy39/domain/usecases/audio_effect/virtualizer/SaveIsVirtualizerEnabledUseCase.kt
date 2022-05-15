package com.hellguy39.domain.usecases.audio_effect.virtualizer

import com.hellguy39.domain.repositories.VirtualizerRepository

class SaveIsVirtualizerEnabledUseCase(private val repository: VirtualizerRepository) {
    operator fun invoke(enabled: Boolean) {
        repository.saveIsEnabled(enabled)
    }
}