package com.hellguy39.collapse.di

import android.content.SharedPreferences
import com.hellguy39.collapse.controllers.audio_effect.*
import com.hellguy39.data.repositories.BassBoostRepositoryImpl
import com.hellguy39.data.repositories.EqualizerRepositoryImpl
import com.hellguy39.data.repositories.VirtualizerRepositoryImpl
import com.hellguy39.domain.usecases.GetEqualizerPropertiesUseCase
import com.hellguy39.domain.usecases.audio_effect.bass_boost.*
import com.hellguy39.domain.usecases.audio_effect.eq.*
import com.hellguy39.domain.usecases.audio_effect.virtualizer.*
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
        eqUseCases: EqUseCases,
        bassBoostUseCases: BassBoostUseCases,
        virtualizerUseCases: VirtualizerUseCases
    ): AudioEffectController {
        return AudioEffectController(
            getEqualizerPropertiesUseCase = getEqualizerPropertiesUseCase,
            eqState = EqState(eqUseCases),
            bassBoostState = BassBoostState(bassBoostUseCases),
            virtualizerState = VirtualizerState(virtualizerUseCases),
            reverbState = ReverbState()
        )
    }

    @Provides
    @Singleton
    fun provideEqRepository(prefs: SharedPreferences): EqualizerRepositoryImpl {
        return EqualizerRepositoryImpl(prefs)
    }

    @Provides
    @Singleton
    fun provideBassBoostRepository(prefs: SharedPreferences): BassBoostRepositoryImpl {
        return BassBoostRepositoryImpl(prefs)
    }

    @Provides
    @Singleton
    fun provideVirtualizerRepository(prefs: SharedPreferences): VirtualizerRepositoryImpl {
        return VirtualizerRepositoryImpl(prefs)
    }

    @Provides
    @Singleton
    fun provideGetEqualizerPropertiesUseCase(): GetEqualizerPropertiesUseCase {
        return GetEqualizerPropertiesUseCase()
    }

    @Provides
    @Singleton
    fun provideEqUseCases(repository: EqualizerRepositoryImpl): EqUseCases {
        return EqUseCases(
            getCustomEqPresetUseCase = GetCustomEqPresetUseCase(repository),
            saveCustomEqPresetUseCase = SaveCustomEqPresetUseCase(repository),
            getEqPresetNumberUseCase = GetEqPresetNumberUseCase(repository),
            saveEqPresetNumberUseCase = SaveEqPresetNumberUseCase(repository),
            getIsEqEnabledUseCase = GetIsEqEnabledUseCase(repository),
            saveIsEqEnabledUseCase = SaveIsEqEnabledUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideBassBoostUseCases(repository: BassBoostRepositoryImpl): BassBoostUseCases {
        return BassBoostUseCases(
            getBassBoostStrengthUseCase = GetBassBoostStrengthUseCase(repository),
            getIsBassBoostEnabledUseCase = GetIsBassBoostEnabledUseCase(repository),
            saveBassBoostStrengthUseCase = SaveBassBoostStrengthUseCase(repository),
            saveIsBassBoostEnabledUseCase = SaveIsBassBoostEnabledUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideVirtualizerUseCases(repository: VirtualizerRepositoryImpl): VirtualizerUseCases {
        return VirtualizerUseCases(
            getIsVirtualizerEnabledUseCase = GetIsVirtualizerEnabledUseCase(repository),
            getVirtualizerStrengthUseCase = GetVirtualizerStrengthUseCase(repository),
            saveIsVirtualizerEnabledUseCase = SaveIsVirtualizerEnabledUseCase(repository),
            saveVirtualizerStrengthUseCase = SaveVirtualizerStrengthUseCase(repository)
        )
    }


}