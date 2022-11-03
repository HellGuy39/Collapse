package com.hellguy39.collapse.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
//    @Provides
//    @Singleton
//    fun provideContext(app: Application): Context {
//        return app.applicationContext
//    }
}