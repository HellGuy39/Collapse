package com.hellguy39.domain.usecases.audio_effect.eq

import com.hellguy39.domain.repositories.EqualizerRepository

class SaveEqPresetNumberUseCase(private val repository: EqualizerRepository) {
    operator fun invoke(presetNumber: Short) {
        repository.savePresetNumber(presetNumber)
    }
}