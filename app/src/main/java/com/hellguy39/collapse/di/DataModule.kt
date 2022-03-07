package com.hellguy39.collapse.di

import android.app.Application
import androidx.room.Room
import com.hellguy39.data.db.TracksDatabase
import com.hellguy39.data.repositories.TracksRepositoryImpl
import com.hellguy39.domain.repositories.TracksRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Singleton
    fun provideTracksDatabase(app: Application): TracksDatabase {
        return Room.databaseBuilder(app, TracksDatabase::class.java, "tracks_db").build()
    }

    @Provides
    @Singleton
    fun provideLocationRepository(tracksDatabase: TracksDatabase) : TracksRepository {
        return TracksRepositoryImpl(tracksDatabase.tracksDao())
    }

}