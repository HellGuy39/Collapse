package com.hellguy39.collapse.controllers.audio_effect

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hellguy39.domain.models.EqualizerPreset
import com.hellguy39.domain.usecases.audio_effect.eq.EqUseCases

class EqState(
    private val eqUseCases: EqUseCases
) {

    companion object {
        const val CUSTOM_PRESET: Short = -1
        //const val CUSTOM_PRESET: Short = -2
    }

    private val isEnabledLiveData = MutableLiveData<Boolean>()
    private val presetNumberLiveData = MutableLiveData<Short>()
    private val bandValuesLiveData = MutableLiveData<MutableList<Short>>(mutableListOf(0,0,0,0,0))

    private val customPresetLiveData = MutableLiveData(eqUseCases.getCustomEqPresetUseCase.invoke())

    fun getCustomPreset(): LiveData<MutableList<Short>> = customPresetLiveData

    fun getIsEnabled(): LiveData<Boolean> = isEnabledLiveData

    fun getPresetNumber(): LiveData<Short> = presetNumberLiveData

    fun getBandValues(): LiveData<MutableList<Short>> = bandValuesLiveData

    fun updateBandValue(band: Short, value: Short) {
        bandValuesLiveData.value = bandValuesLiveData.value.apply {
            this?.set(band.toInt(), value)
        }

        if (presetNumberLiveData.value != CUSTOM_PRESET)
            presetNumberLiveData.value = CUSTOM_PRESET

        if (presetNumberLiveData.value == CUSTOM_PRESET)
            saveCustomPreset()

        savePresetNumber()
    }

    fun updateIsEnabled(isEnabled: Boolean) {
        isEnabledLiveData.value = isEnabled
        saveIsEnabled()
    }

    fun updatePreset(preset: Short, values: MutableList<Short>) {
        bandValuesLiveData.value = values
        presetNumberLiveData.value = preset
        savePresetNumber()
    }

    fun loadSavedState(presets: List<EqualizerPreset>) {
        isEnabledLiveData.value = eqUseCases.getIsEqEnabledUseCase.invoke()
        presetNumberLiveData.value = eqUseCases.getEqPresetNumberUseCase.invoke().also {
            when(it) {
                CUSTOM_PRESET -> {
                    bandValuesLiveData.value = eqUseCases.getCustomEqPresetUseCase.invoke()
                }
                else -> {
                    bandValuesLiveData.value = presets[it.toInt()].bandValues
                }
            }
        }
    }

    private fun saveCustomPreset() {
        bandValuesLiveData.value?.let {
            eqUseCases.saveCustomEqPresetUseCase.invoke(it)
        }
    }

    private fun saveIsEnabled() {
        isEnabledLiveData.value?.let {
            eqUseCases.saveIsEqEnabledUseCase.invoke(it)
        }
    }

    private fun savePresetNumber() {
        presetNumberLiveData.value?.let {
            eqUseCases.saveEqPresetNumberUseCase.invoke(it)
        }
    }
}