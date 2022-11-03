package com.hellguy39.collapse.core.data.di

import android.app.Application
import com.hellguy39.collapse.core.data.repository_impl.AlbumRepositoryImpl
import com.hellguy39.collapse.core.data.repository_impl.ArtistRepositoryImpl
import com.hellguy39.collapse.core.data.repository_impl.SongRepositoryImpl
import com.hellguy39.collapse.core.domain.repository.AlbumRepository
import com.hellguy39.collapse.core.domain.repository.ArtistRepository
import com.hellguy39.collapse.core.domain.repository.SongRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideArtistRepository(
        app: Application
    ): ArtistRepository {
        return ArtistRepositoryImpl(app)
    }

    @Provides
    @Singleton
    fun provideAlbumRepository(
        app: Application
    ): AlbumRepository {
        return AlbumRepositoryImpl(app)
    }

    @Provides
    @Singleton
    fun provideSongRepository(
        app: Application
    ): SongRepository {
        return SongRepositoryImpl(app)
    }

}