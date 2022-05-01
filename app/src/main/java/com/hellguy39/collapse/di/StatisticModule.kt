package com.hellguy39.collapse.di

import android.content.SharedPreferences
import com.hellguy39.collapse.utils.StatisticController
import com.hellguy39.data.repositories.StatisticRepositoryImpl
import com.hellguy39.domain.usecases.statistics.GetStatisticsUseCase
import com.hellguy39.domain.usecases.statistics.SaveTotalListeningTimeUseCase
import com.hellguy39.domain.usecases.statistics.StatisticsUseCases
import com.hellguy39.domain.usecases.statistics.UpdateStatisticsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class StatisticModule {

    @Provides
    @Singleton
    fun provideStatisticController(statisticsUseCases: StatisticsUseCases): StatisticController {
        return StatisticController(
            statisticsUseCases = statisticsUseCases
        )
    }

    @Provides
    @Singleton
    fun provideStatisticRepository(prefs: SharedPreferences): StatisticRepositoryImpl {
        return StatisticRepositoryImpl(prefs)
    }

    @Provides
    @Singleton
    fun provideStatisticUseCases(repository: StatisticRepositoryImpl): StatisticsUseCases {
        return StatisticsUseCases(
            getStatisticsUseCase = GetStatisticsUseCase(repository),
            updateStatisticsUseCase = UpdateStatisticsUseCase(repository),
            saveTotalListeningTimeUseCase = SaveTotalListeningTimeUseCase(repository)
        )
    }

}