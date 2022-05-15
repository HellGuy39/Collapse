package com.hellguy39.domain.usecases.statistics

data class StatisticsUseCases(
    val getStatisticsUseCase: GetStatisticsUseCase,
    val updateStatisticsUseCase: UpdateStatisticsUseCase,
    val saveTotalListeningTimeUseCase: SaveTotalListeningTimeUseCase
)