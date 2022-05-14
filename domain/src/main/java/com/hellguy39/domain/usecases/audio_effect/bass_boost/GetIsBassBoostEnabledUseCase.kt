package com.hellguy39.domain.usecases.audio_effect.bass_boost

import com.hellguy39.domain.repositories.BassBoostRepository
import com.hellguy39.domain.repositories.EqualizerRepository

class GetIsBassBoostEnabledUseCase(private val repository: BassBoostRepository) {
    operator fun invoke(): Boolean {
        return repository.getIsEnabled()
    }
}