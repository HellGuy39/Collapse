package com.hellguy39.data.repositories

import android.content.SharedPreferences
import com.hellguy39.domain.models.EqualizerSettings
import com.hellguy39.domain.repositories.EqualizerSettingsRepository

private const val IS_ENABLED = "isEnabled"
private const val BAND_1 = "band1"
private const val BAND_2 = "band2"
private const val BAND_3 = "band3"
private const val BAND_4 = "band4"
private const val BAND_5 = "band5"
private const val BAND_BASS_BOOST = "band_bass_boost"
private const val BAND_VIRTUALIZER = "band_virtualizer"
private const val PRESET = "preset"

class EqualizerRepositoryImpl(private val sharedPreferences: SharedPreferences): EqualizerSettingsRepository {
    override fun getEqualizerSettings(): EqualizerSettings {

        val equalizerSettings = EqualizerSettings(
            isEnabled = sharedPreferences.getBoolean(IS_ENABLED, false),
            band1Level = sharedPreferences.getFloat(BAND_1, 0f),
            band2Level = sharedPreferences.getFloat(BAND_2, 0f),
            band3Level = sharedPreferences.getFloat(BAND_3, 0f),
            band4Level = sharedPreferences.getFloat(BAND_4, 0f),
            band5Level = sharedPreferences.getFloat(BAND_5, 0f),
            bandBassBoost = sharedPreferences.getFloat(BAND_BASS_BOOST, 0f),
            bandVirtualizer = sharedPreferences.getFloat(BAND_VIRTUALIZER, 0f),
            preset = sharedPreferences.getInt(PRESET, 0)
        )

        return equalizerSettings
    }

    override fun saveEqualizerSettings(equalizerSettings: EqualizerSettings) {
        sharedPreferences.edit().apply {
            this.putBoolean(IS_ENABLED, equalizerSettings.isEnabled)
            this.putFloat(BAND_1, equalizerSettings.band1Level)
            this.putFloat(BAND_2, equalizerSettings.band2Level)
            this.putFloat(BAND_3, equalizerSettings.band3Level)
            this.putFloat(BAND_4, equalizerSettings.band4Level)
            this.putFloat(BAND_5, equalizerSettings.band5Level)
            this.putFloat(BAND_BASS_BOOST, equalizerSettings.bandBassBoost)
            this.putFloat(BAND_VIRTUALIZER, equalizerSettings.bandVirtualizer)
            this.putInt(PRESET, equalizerSettings.preset)
        }.apply()
    }

    override fun savePreset(preset: Int) {
        sharedPreferences.edit().apply {
            this.putInt(PRESET, preset)
        }.apply()
    }

    override fun saveIsEnabled(isEnabled: Boolean) {
        sharedPreferences.edit().apply {
            this.putBoolean(IS_ENABLED, isEnabled)
        }.apply()
    }

    override fun saveBassBoostValue(bass: Float) {
        sharedPreferences.edit().apply {
            this.putFloat(BAND_BASS_BOOST, bass)
        }.apply()
    }

    override fun saveVirtualizer(virtualize: Float) {
        sharedPreferences.edit().apply {
            this.putFloat(BAND_VIRTUALIZER, virtualize)
        }.apply()
    }

    override fun saveBandsLevel(levels: List<Float>) {
        sharedPreferences.edit().apply {
            this.putFloat(BAND_1, levels[0])
            this.putFloat(BAND_2, levels[1])
            this.putFloat(BAND_3, levels[2])
            this.putFloat(BAND_4, levels[3])
            this.putFloat(BAND_5, levels[4])
        }.apply()
    }
}