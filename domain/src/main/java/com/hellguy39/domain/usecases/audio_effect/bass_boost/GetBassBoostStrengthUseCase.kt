package com.hellguy39.domain.usecases.audio_effect.bass_boost

import com.hellguy39.domain.repositories.BassBoostRepository

class GetBassBoostStrengthUseCase(private val repository: BassBoostRepository) {
    operator fun invoke(): Short {
        return repository.getStrength()
    }
}