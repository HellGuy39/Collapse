package com.hellguy39.collapse.di

import android.content.SharedPreferences
import com.hellguy39.data.repositories.EqualizerRepositoryImpl
import com.hellguy39.data.repositories.FavouritesRepositoryImpl
import com.hellguy39.data.repositories.PlaylistsRepositoryImpl
import com.hellguy39.data.repositories.RadioStationsRepositoryImpl
import com.hellguy39.domain.repositories.TracksRepository
import com.hellguy39.domain.usecases.ConvertBitmapToByteArrayUseCase
import com.hellguy39.domain.usecases.ConvertByteArrayToBitmapUseCase
import com.hellguy39.domain.usecases.GetImageBitmapUseCase
import com.hellguy39.domain.usecases.eq_settings.EqualizerSettingsUseCases
import com.hellguy39.domain.usecases.eq_settings.GetEqualizerSettingsUseCase
import com.hellguy39.domain.usecases.eq_settings.SaveEqualizerSettingsUseCase
import com.hellguy39.domain.usecases.favourites.AddFavouriteTrackUseCase
import com.hellguy39.domain.usecases.favourites.DeleteFavouriteTrackUseCase
import com.hellguy39.domain.usecases.favourites.FavouriteTracksUseCases
import com.hellguy39.domain.usecases.favourites.GetAllFavouriteTracksUseCase
import com.hellguy39.domain.usecases.playlist.*
import com.hellguy39.domain.usecases.radio.*
import com.hellguy39.domain.usecases.tracks.GetAllTracksUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DomainModule {

    @Provides
    @Singleton
    fun provideEqualizerSettingsUseCases(repository: EqualizerRepositoryImpl): EqualizerSettingsUseCases {
        return EqualizerSettingsUseCases(
            getEqualizerSettings = GetEqualizerSettingsUseCase(repository),
            saveEqualizerSettingsUseCase = SaveEqualizerSettingsUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideGetImageBitmapUseCase(): GetImageBitmapUseCase {
        return GetImageBitmapUseCase()
    }

    @Provides
    @Singleton
    fun providePlaylistUseCases(repository: PlaylistsRepositoryImpl): PlaylistUseCases {
        return PlaylistUseCases(
            addPlaylistUseCase = AddPlaylistUseCase(repository),
            deletePlaylistUseCase = DeletePlaylistUseCase(repository),
            updatePlaylistUseCase = UpdatePlaylistUseCase(repository),
            getAllPlaylistsUseCase = GetAllPlaylistsUseCase(repository),
            getPlaylistByIdUseCase = GetPlaylistByIdUseCase(repository)
        )
    }

    @Singleton
    @Provides
    fun provideRadioStationUseCases(repository: RadioStationsRepositoryImpl): RadioStationUseCases {
        return RadioStationUseCases(
            addRadioStationUseCase = AddRadioStationUseCase(repository),
            editRadioStationUseCase = EditRadioStationUseCase(repository),
            deleteRadioStationUseCase = DeleteRadioStationUseCase(repository),
            getAllRadioStationsUseCase = GetAllRadioStationsUseCase(repository),
            getRadioStationByIdUseCase = GetRadioStationByIdUseCase(repository),
            getRadioStationsWithQueryUseCase = GetRadioStationsWithQueryUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideGetAllTracksUseCase(tracksRepositoryImpl: TracksRepository): GetAllTracksUseCase {
        return GetAllTracksUseCase(tracksRepositoryImpl)
    }


    @Provides
    @Singleton
    fun provideFavouriteTracksUseCases(repository: FavouritesRepositoryImpl): FavouriteTracksUseCases {
        return FavouriteTracksUseCases(
            addFavouriteTrackUseCase = AddFavouriteTrackUseCase(repository),
            deleteFavouriteTrackUseCase = DeleteFavouriteTrackUseCase(repository),
            getAllFavouriteTracksUseCase = GetAllFavouriteTracksUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideConvertBitmapToByteArrayUseCase(): ConvertBitmapToByteArrayUseCase {
        return ConvertBitmapToByteArrayUseCase()
    }

    @Provides
    @Singleton
    fun provideConvertByteArrayToBitmapUseCase(): ConvertByteArrayToBitmapUseCase {
        return ConvertByteArrayToBitmapUseCase()
    }


}