package com.hellguy39.domain.repositories

import com.hellguy39.domain.models.StatisticsModel

interface StatisticRepository {
    fun getStatistic(): StatisticsModel
    fun updateStatistic(statisticsModel: StatisticsModel)
    fun saveTotalListeningTime(time: Long)
}