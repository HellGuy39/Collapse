package com.hellguy39.domain.usecases.audio_effect.bass_boost

import com.hellguy39.domain.repositories.BassBoostRepository

class SaveBassBoostStrengthUseCase(private val repository: BassBoostRepository) {
    operator fun invoke(strength: Short) {
        repository.saveStrength(strength)
    }
}