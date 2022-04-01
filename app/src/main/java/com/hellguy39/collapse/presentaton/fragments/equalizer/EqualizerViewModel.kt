package com.hellguy39.collapse.presentaton.fragments.equalizer

import androidx.lifecycle.ViewModel
import com.hellguy39.domain.models.EqualizerSettings
import com.hellguy39.domain.usecases.eq_settings.EqualizerSettingsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EqualizerViewModel @Inject constructor(
    private val equalizerSettingsUseCases: EqualizerSettingsUseCases
) : ViewModel() {

    fun saveEqualizerSettings(equalizerSettings: EqualizerSettings) {
        equalizerSettingsUseCases.saveEqualizerSettingsUseCase.invoke(equalizerSettings)
    }

    fun getEqualizerSettings(): EqualizerSettings {
        return equalizerSettingsUseCases.getEqualizerSettings.invoke()
    }

}