package com.hellguy39.domain.usecases.statistics

import com.hellguy39.domain.models.StatisticsModel
import com.hellguy39.domain.repositories.StatisticRepository

class GetStatisticsUseCase(private val repository: StatisticRepository) {
    operator fun invoke(): StatisticsModel {
        return repository.getStatistic()
    }
}