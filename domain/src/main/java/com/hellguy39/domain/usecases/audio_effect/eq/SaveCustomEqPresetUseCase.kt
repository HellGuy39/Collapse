package com.hellguy39.domain.usecases.audio_effect.eq

import com.hellguy39.domain.repositories.EqualizerRepository

class SaveCustomEqPresetUseCase(private val repository: EqualizerRepository) {
    operator fun invoke(values: MutableList<Short>) {
        repository.savePreset(values)
    }
}