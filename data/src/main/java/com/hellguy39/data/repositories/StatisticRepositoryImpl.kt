package com.hellguy39.data.repositories

import android.content.SharedPreferences
import com.hellguy39.domain.models.StatisticsModel
import com.hellguy39.domain.repositories.StatisticRepository

private const val TOTAL_LISTENING_TIME = "total_listening_time"

class StatisticRepositoryImpl(
    private val prefs: SharedPreferences,
): StatisticRepository{
    override fun getStatistic(): StatisticsModel {
        return StatisticsModel(
            totalListeningTime = prefs.getLong(TOTAL_LISTENING_TIME, 0)
        )
    }

    override fun updateStatistic(statisticsModel: StatisticsModel) {
        prefs.edit().apply {
            this.putLong(TOTAL_LISTENING_TIME, statisticsModel.totalListeningTime)
        }.apply()
    }

    override fun saveTotalListeningTime(time: Long) {
        prefs.edit().apply {
            this.putLong(TOTAL_LISTENING_TIME, time)
        }.apply()
    }
}