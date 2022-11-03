package com.hellguy39.collapse.di

import android.app.Application
import com.hellguy39.collapse.core.data.repository_impl.AlbumRepositoryImpl
import com.hellguy39.collapse.core.data.repository_impl.ArtistRepositoryImpl
import com.hellguy39.collapse.core.domain.repository.AlbumRepository
import com.hellguy39.collapse.core.domain.repository.ArtistRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

//    @Provides
//    @Singleton
//    fun provideArtistRepository(
//        app: Application
//    ) : ArtistRepository {
//        return ArtistRepositoryImpl(app)
//    }
//
//    @Provides
//    @Singleton
//    fun provideAlbumRepository(
//        app: Application
//    ) : AlbumRepository {
//        return AlbumRepositoryImpl(app)
//    }

}