package com.hellguy39.domain.usecases.statistics

import com.hellguy39.domain.repositories.StatisticRepository

class SaveTotalListeningTimeUseCase(private val repository: StatisticRepository) {
    operator fun invoke(time: Long) {
        repository.saveTotalListeningTime(time)
    }
}