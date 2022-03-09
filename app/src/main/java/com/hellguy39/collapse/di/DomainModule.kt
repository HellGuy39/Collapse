package com.hellguy39.collapse.di

import com.hellguy39.data.repositories.TracksRepositoryImpl
import com.hellguy39.domain.repositories.TracksRepository
import com.hellguy39.domain.usecases.AddTrackUseCase
import com.hellguy39.domain.usecases.GetAllTracksUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DomainModule {


}