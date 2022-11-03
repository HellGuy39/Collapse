package com.hellguy39.collapse.songs.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object SongModule {

//    @Provides
//    @Singleton
//    fun provideSongRepository(
//        app: Application
//    ) : SongRepository {
//        return SongRepositoryImpl(app)
//    }
//
//    @Provides
//    @Singleton
//    fun provideSongUseCase(
//        repository: SongRepository
//    ) : GetAllSongsUseCase {
//        return GetAllSongsUseCase(repository)
//    }


}