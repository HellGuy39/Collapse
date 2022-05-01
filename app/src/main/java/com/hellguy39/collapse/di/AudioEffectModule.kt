package com.hellguy39.collapse.di

import android.content.SharedPreferences
import com.hellguy39.collapse.utils.AudioEffectController
import com.hellguy39.data.repositories.EqualizerRepositoryImpl
import com.hellguy39.domain.usecases.GetEqualizerPropertiesUseCase
import com.hellguy39.domain.usecases.eq_settings.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AudioEffectModule {

    @Provides
    @Singleton
    fun provideAudioEffectController(
        getEqualizerPropertiesUseCase: GetEqualizerPropertiesUseCase,
        equalizerSettingsUseCases: EqualizerSettingsUseCases
    ): AudioEffectController {
        return AudioEffectController(
            getEqualizerPropertiesUseCase,
            equalizerSettingsUseCases
        )
    }

    @Provides
    @Singleton
    fun provideEqualizerSettingsRepository(prefs: SharedPreferences): EqualizerRepositoryImpl {
        return EqualizerRepositoryImpl(prefs)
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
            saveBandLevelUseCase = SaveBandLevelUseCase(repository),
            saveReverbPresetUseCase = SaveReverbPresetUseCase(repository),
            saveReverbSwitchUseCase = SaveReverbSwitchUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideGetEqualizerPropertiesUseCase(): GetEqualizerPropertiesUseCase {
        return GetEqualizerPropertiesUseCase()
    }
}