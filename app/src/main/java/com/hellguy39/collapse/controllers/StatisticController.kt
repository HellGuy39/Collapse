package com.hellguy39.collapse.controllers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hellguy39.domain.models.StatisticsModel
import com.hellguy39.domain.usecases.statistics.StatisticsUseCases

class StatisticController(
    private val statisticsUseCases: StatisticsUseCases
) {
    private val statisticModelLiveData = MutableLiveData<StatisticsModel>()
    private val totalListeningTimeLiveData = MutableLiveData<Long>(0)

    init {
        statisticModelLiveData.value = statisticsUseCases.getStatisticsUseCase.invoke()
        statisticModelLiveData.value?.let {
            totalListeningTimeLiveData.value = it.totalListeningTime
        }
    }

    fun getStatistic(): LiveData<StatisticsModel> = statisticModelLiveData

    fun getTotalListeningTime(): LiveData<Long> = totalListeningTimeLiveData

    fun updateTotalListeningTime(addDuration: Long) {
        statisticModelLiveData.value?.let { statisticsModel ->
            statisticsModel.totalListeningTime += addDuration
            totalListeningTimeLiveData.value = statisticsModel.totalListeningTime
            saveTotalListeningTime(statisticsModel.totalListeningTime)
        }
    }

    private fun saveTotalListeningTime(totalListeningTime: Long) {
        statisticsUseCases.saveTotalListeningTimeUseCase.invoke(totalListeningTime)
    }

}