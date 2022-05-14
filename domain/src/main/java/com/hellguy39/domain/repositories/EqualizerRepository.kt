package com.hellguy39.domain.repositories

interface EqualizerRepository {

    fun savePreset(values: MutableList<Short>)

    fun getPreset(): MutableList<Short>

    fun getPresetNumber(): Short

    fun savePresetNumber(preset: Short)

    fun getIsEnabled(): Boolean

    fun saveIsEnabled(enabled: Boolean)

}