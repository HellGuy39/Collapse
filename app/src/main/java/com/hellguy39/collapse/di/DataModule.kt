package com.hellguy39.collapse.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.hellguy39.data.db.RadioStationsDatabase
import com.hellguy39.data.repositories.RadioStationsRepositoryImpl
import com.hellguy39.data.repositories.TracksRepositoryImpl
import com.hellguy39.domain.repositories.RadioStationsRepository
import com.hellguy39.domain.repositories.TracksRepository
import com.hellguy39.domain.usecases.GetAllTracksUseCase
import com.hellguy39.domain.usecases.GetImageBitmapUseCase
import com.hellguy39.domain.usecases.radio.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideContext(app: Application): Context {
        return app.applicationContext
    }

    @Provides
    @Singleton
    fun provideRadioStationsDatabase(app: Application): RadioStationsDatabase {
        return Room.databaseBuilder(
            app,
            RadioStationsDatabase::class.java,
            "radio_stations_db"
        ).build()
    }


    @Provides
    @Singleton
    fun provideLocationRepository(context: Context) : TracksRepository {
        return TracksRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun provideRadioStationRepository(radioStationsDatabase: RadioStationsDatabase): RadioStationsRepositoryImpl {
        return RadioStationsRepositoryImpl(radioStationsDatabase.radioStationsDao())
    }

    @Singleton
    @Provides
    fun provideRadioStationUseCases(radioStationsRepository: RadioStationsRepositoryImpl): RadioStationUseCases {
        return RadioStationUseCases(
            addRadioStationUseCase = AddRadioStationUseCase(radioStationsRepository),
            editRadioStationUseCase = EditRadioStationUseCase(radioStationsRepository),
            deleteRadioStationUseCase = DeleteRadioStationUseCase(radioStationsRepository),
            getAllRadioStationsUseCase = GetAllRadioStationsUseCase(radioStationsRepository),
            getRadioStationByIdUseCase = GetRadioStationByIdUseCase(radioStationsRepository),
            getRadioStationsWithQueryUseCase = GetRadioStationsWithQueryUseCase(radioStationsRepository)
        )
    }

    @Provides
    @Singleton
    fun provideGetImageBitmapUseCase(): GetImageBitmapUseCase {
        return GetImageBitmapUseCase()
    }

    @Provides
    @Singleton
    fun provideGetAllTracksUseCase(tracksRepositoryImpl: TracksRepository): GetAllTracksUseCase {
        return GetAllTracksUseCase(tracksRepositoryImpl)
    }

}