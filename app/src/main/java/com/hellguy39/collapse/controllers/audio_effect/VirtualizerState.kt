package com.hellguy39.collapse.controllers.audio_effect

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hellguy39.domain.usecases.audio_effect.virtualizer.VirtualizerUseCases

class VirtualizerState(
    private val virtualizerUseCases: VirtualizerUseCases
) {

    private val isEnabledLiveData = MutableLiveData<Boolean>()
    private val virtualizerStrengthLiveData = MutableLiveData<Short>()

    fun getIsEnabled(): LiveData<Boolean> = isEnabledLiveData

    fun getVirtualizerStrength(): LiveData<Short> = virtualizerStrengthLiveData

    fun updateIsEnabled(enabled: Boolean) {
        isEnabledLiveData.value = enabled
        saveIsEnabled()
    }

    fun updateVirtualizerStrength(strength: Short) {
        virtualizerStrengthLiveData.value = strength
        saveVirtualizerStrength()
    }

    fun loadSavedState() {
        isEnabledLiveData.value = virtualizerUseCases.getIsVirtualizerEnabledUseCase.invoke()
        virtualizerStrengthLiveData.value = virtualizerUseCases.getVirtualizerStrengthUseCase.invoke()
    }

    private fun saveIsEnabled() {
        isEnabledLiveData.value?.let {
            virtualizerUseCases.saveIsVirtualizerEnabledUseCase.invoke(it)
        }
    }

    private fun saveVirtualizerStrength() {
        virtualizerStrengthLiveData.value?.let {
            virtualizerUseCases.saveVirtualizerStrengthUseCase.invoke(it)
        }
    }
}