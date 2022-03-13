package com.hellguy39.collapse.di

import android.app.Application
import android.content.Context
import com.hellguy39.data.repositories.TracksRepositoryImpl
import com.hellguy39.domain.repositories.TracksRepository
import com.hellguy39.domain.usecases.GetAllTracksUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    /*@Provides
    @Singleton
    fun provideTracksDatabase(app: Application): TracksDatabase {
        return Room.databaseBuilder(
            app,
            TracksDatabase::class.java,
            "tracks_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideLocationRepository(tracksDatabase: TracksDatabase) : TracksRepository {
        return TracksRepositoryImpl(tracksDatabase.tracksDao())
    }

    @Provides
    @Singleton
    fun provideAddTrackUseCase(tracksRepositoryImpl: TracksRepository): AddTrackUseCase {
        return AddTrackUseCase(tracksRepositoryImpl)
    }
*/
    @Provides
    @Singleton
    fun provideContext(app: Application): Context {
        return app.applicationContext
    }

    @Provides
    @Singleton
    fun provideLocationRepository(context: Context) : TracksRepository {
        return TracksRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun provideGetAllTracksUseCase(tracksRepositoryImpl: TracksRepository): GetAllTracksUseCase {
        return GetAllTracksUseCase(tracksRepositoryImpl)
    }

}