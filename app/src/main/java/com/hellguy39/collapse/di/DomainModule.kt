package com.hellguy39.collapse.di

import com.hellguy39.data.repositories.*
import com.hellguy39.domain.repositories.TracksRepository
import com.hellguy39.domain.usecases.*
import com.hellguy39.domain.usecases.eq_settings.*
import com.hellguy39.domain.usecases.favourites.*
import com.hellguy39.domain.usecases.playlist.*
import com.hellguy39.domain.usecases.radio.*
import com.hellguy39.domain.usecases.state.GetSavedServiceStateUseCase
import com.hellguy39.domain.usecases.state.InsertSavedServiceStateUseCase
import com.hellguy39.domain.usecases.state.SavedServiceStateUseCases
import com.hellguy39.domain.usecases.tracks.*
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
    fun provideSavedServiceStateUseCases(repository: SavedServiceStateRepositoryImpl): SavedServiceStateUseCases {
        return SavedServiceStateUseCases(
            getSavedServiceStateUseCase = GetSavedServiceStateUseCase(repository),
            insertSavedServiceStateUseCase = InsertSavedServiceStateUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideEqualizerSettingsUseCases(repository: EqualizerRepositoryImpl): EqualizerSettingsUseCases {
        return EqualizerSettingsUseCases(
            getEqualizerSettingsUseCase = GetEqualizerSettingsUseCase(repository),
            saveEqualizerSettingsUseCase = SaveEqualizerSettingsUseCase(repository),
            saveEqPresetUseCase = SaveEqPresetUseCase(repository),
            saveEqSwitchUseCase = SaveEqSwitchUseCase(repository),
            saveEqBandsLevelUseCase = SaveEqBandsLevelUseCase(repository),
            saveBassBoostValueUseCase = SaveBassBoostValueUseCase(repository),
            saveVirtualizerValueUseCase = SaveVirtualizerValueUseCase(repository),
            saveBassBoostSwitchUseCase = SaveBassBoostSwitchUseCase(repository),
            saveVirtualizerSwitchUseCase = SaveVirtualizerSwitchUseCase(repository),
            saveBandLevelUseCase = SaveBandLevelUseCase(repository)
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
    fun provideTracksUseCases(repository: TracksRepository): TracksUseCases {
        return TracksUseCases(
            getAllTracksUseCase = GetAllTracksUseCase(repository),
            getTracksByArtistUseCase = GetTracksByArtistUseCase(repository),
            getAllArtistsUseCase = GetAllArtistsUseCase(repository),
            getAllTrackByArtistUseCase = GetAllTrackByArtistUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideFavouriteTracksUseCases(repository: FavouritesRepositoryImpl): FavouriteTracksUseCases {
        return FavouriteTracksUseCases(
            addFavouriteTrackUseCase = AddFavouriteTrackUseCase(repository),
            deleteFavouriteTrackUseCase = DeleteFavouriteTrackUseCase(repository),
            getAllFavouriteTracksUseCase = GetAllFavouriteTracksUseCase(repository),
            deleteFromFavouritesWithoutIdUseCase = DeleteFromFavouritesWithoutIdUseCase(repository),
            isTrackFavouriteUseCase = IsTrackFavouriteUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideGetEqualizerPropertiesUseCase(): GetEqualizerPropertiesUseCase {
        return GetEqualizerPropertiesUseCase()
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

    @Provides
    @Singleton
    fun provideGetColorFromThemeUseCase(): GetColorFromThemeUseCase {
        return GetColorFromThemeUseCase()
    }

}