package com.hellguy39.collapse.di

import android.app.Application
import android.content.Context
import com.google.android.exoplayer2.ExoPlayer
import com.hellguy39.collapse.utils.AudioEffectController
import com.hellguy39.domain.usecases.GetEqualizerPropertiesUseCase
import com.hellguy39.domain.usecases.eq_settings.EqualizerSettingsUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideContext(app: Application): Context {
        return app.applicationContext
    }
}