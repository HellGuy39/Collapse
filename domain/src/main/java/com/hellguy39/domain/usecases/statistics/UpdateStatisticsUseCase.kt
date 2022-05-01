package com.hellguy39.domain.usecases.statistics

import com.hellguy39.domain.models.StatisticsModel
import com.hellguy39.domain.repositories.StatisticRepository

class UpdateStatisticsUseCase(private val repository: StatisticRepository) {
    operator fun invoke(statisticsModel: StatisticsModel) {
        repository.updateStatistic(statisticsModel)
    }
}