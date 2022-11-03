package com.hellguy39.collapse.di

//import android.app.Application
//import android.content.Context
//import android.content.SharedPreferences
//import androidx.room.Room
//import com.hellguy39.data.db.FavouritesDatabase
//import com.hellguy39.data.db.PlaylistsDatabase
//import com.hellguy39.data.db.RadioStationsDatabase
//import com.hellguy39.data.repositories.*
//import com.hellguy39.domain.repositories.TracksRepository
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.components.SingletonComponent
//import javax.inject.Singleton
//
//private const val RADIO_STATIONS_DB_NAME = "radio_stations_db"
//private const val FAVOURITES_DB_NAME = "favourites_db"
//private const val PLAYLISTS_DB_NAME = "playlists_db"
//private const val SAVED_STATE_DB_NAME = "saved_state_db"
//const val PREFS_NAME = "prefs_name"
//
//@Module
//@InstallIn(SingletonComponent::class)
//class DataModule {
//
//    @Provides
//    @Singleton
//    fun provideSharedPreferences(app: Application): SharedPreferences {
//        return app.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
//    }
//
//    @Provides
//    @Singleton
//    fun provideFavouritesDatabase(app: Application): FavouritesDatabase {
//        return Room.databaseBuilder(
//            app,
//            FavouritesDatabase::class.java,
//            FAVOURITES_DB_NAME
//        ).build()
//    }
//
//    @Singleton
//    @Provides
//    fun providePlaylistsDatabase(app: Application): PlaylistsDatabase {
//        return Room.databaseBuilder(
//            app,
//            PlaylistsDatabase::class.java,
//            PLAYLISTS_DB_NAME
//        ).build()
//    }
//
//    @Provides
//    @Singleton
//    fun provideRadioStationsDatabase(app: Application): RadioStationsDatabase {
//        return Room.databaseBuilder(
//            app,
//            RadioStationsDatabase::class.java,
//            RADIO_STATIONS_DB_NAME
//        ).build()
//    }
//
//    @Provides
//    @Singleton
//    fun provideTracksRepository(context: Context) : TracksRepository {
//        return TracksRepositoryImpl(context)
//    }
//
//    @Provides
//    @Singleton
//    fun provideRadioStationRepository(database: RadioStationsDatabase): RadioStationsRepositoryImpl {
//        return RadioStationsRepositoryImpl(database.dao())
//    }
//
//
//    @Singleton
//    @Provides
//    fun providePlaylistsRepository(database: PlaylistsDatabase): PlaylistsRepositoryImpl {
//        return PlaylistsRepositoryImpl(database.dao())
//    }
//
//    @Provides
//    @Singleton
//    fun provideFavouritesRepository(database: FavouritesDatabase): FavouritesRepositoryImpl {
//        return FavouritesRepositoryImpl(favouritesDao = database.dao())
//    }
//
//    @Provides
//    @Singleton
//    fun provideSavedServiceStateRepository(prefs: SharedPreferences): SavedServiceStateRepositoryImpl {
//        return SavedServiceStateRepositoryImpl(prefs = prefs)
//    }
//
//    @Provides
//    @Singleton
//    fun provideAppSettingsRepository(prefs: SharedPreferences): AppSettingsRepositoryImpl {
//        return AppSettingsRepositoryImpl(prefs = prefs)
//    }
//}