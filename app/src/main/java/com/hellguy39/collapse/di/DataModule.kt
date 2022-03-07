package com.hellguy39.collapse.di

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class DataModule {

    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.soundcloud.com")
            .build()
    }

}