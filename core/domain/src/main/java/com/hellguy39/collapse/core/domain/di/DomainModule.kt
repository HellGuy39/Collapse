package com.hellguy39.collapse.core.domain.di

import com.hellguy39.collapse.core.domain.repository.AlbumRepository
import com.hellguy39.collapse.core.domain.repository.ArtistRepository
import com.hellguy39.collapse.core.domain.repository.SongRepository
import com.hellguy39.collapse.core.domain.usecase.album.GetAlbumByIdUseCase
import com.hellguy39.collapse.core.domain.usecase.album.GetAllAlbumsUseCase
import com.hellguy39.collapse.core.domain.usecase.album.GetAllSongsOfAlbumUseCase
import com.hellguy39.collapse.core.domain.usecase.artist.GetAllArtistsUseCase
import com.hellguy39.collapse.core.domain.usecase.artist.GetAllSongsOfArtistUseCase
import com.hellguy39.collapse.core.domain.usecase.artist.GetArtistByIdUseCase
import com.hellguy39.collapse.core.domain.usecase.song.GetAllSongsUseCase
import com.hellguy39.collapse.core.domain.wrapper.AlbumUseCaseWrapper
import com.hellguy39.collapse.core.domain.wrapper.ArtistUseCaseWrapper
import com.hellguy39.collapse.core.domain.wrapper.SongUseCaseWrapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    @Singleton
    fun provideAlbumsUseCaseWrapper(
        repository: AlbumRepository
    ): AlbumUseCaseWrapper {
        return AlbumUseCaseWrapper(
            getAllAlbumsUseCase = GetAllAlbumsUseCase(repository),
            getAlbumByIdUseCase = GetAlbumByIdUseCase(repository),
            getAllSongsOfAlbumUseCase = GetAllSongsOfAlbumUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideSongUseCaseWrapper(
        repository: SongRepository
    ): SongUseCaseWrapper {
        return SongUseCaseWrapper(
            getAllSongsUseCase = GetAllSongsUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideArtistsUseCaseWrapper(
        repository: ArtistRepository
    ): ArtistUseCaseWrapper {
        return ArtistUseCaseWrapper(
            getAllArtistsUseCase = GetAllArtistsUseCase(repository),
            getAllSongsOfArtistUseCase = GetAllSongsOfArtistUseCase(repository),
            getArtistByIdUseCase = GetArtistByIdUseCase(repository)
        )
    }


}