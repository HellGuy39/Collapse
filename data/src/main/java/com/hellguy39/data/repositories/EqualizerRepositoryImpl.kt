package com.hellguy39.data.repositories

import android.content.Context
import android.content.SharedPreferences
import com.hellguy39.domain.models.EqualizerSettings
import com.hellguy39.domain.repositories.EqualizerSettingsRepository

private const val IS_ENABLED = "isEnabled"
private const val BAND_1 = "band1"
private const val BAND_2 = "band1"
private const val BAND_3 = "band1"
private const val BAND_4 = "band1"
private const val BAND_5 = "band1"
private const val BAND_BASS_BOOST = "band_bass_boost"
private const val BAND_VIRTUALIZER = "band_virtualizer"

class EqualizerRepositoryImpl(private val sharedPreferences: SharedPreferences): EqualizerSettingsRepository {
    override fun getEqualizerSettings(): EqualizerSettings {

        return EqualizerSettings(
            isEnabled = sharedPreferences.getBoolean(IS_ENABLED, false),
            band1Level = sharedPreferences.getFloat(BAND_1, 0f),
            band2Level = sharedPreferences.getFloat(BAND_2, 0f),
            band3Level = sharedPreferences.getFloat(BAND_3, 0f),
            band4Level = sharedPreferences.getFloat(BAND_4, 0f),
            band5Level = sharedPreferences.getFloat(BAND_5, 0f),
            bandBassBoost = sharedPreferences.getFloat(BAND_BASS_BOOST, 0f),
            bandVirtualizer = sharedPreferences.getFloat(BAND_VIRTUALIZER, 0f)
        )
    }

    override fun saveEqualizerSettings(equalizerSettings: EqualizerSettings) {
        sharedPreferences.edit().apply {
            this.putBoolean(IS_ENABLED, equalizerSettings.isEnabled)
            this.putFloat(BAND_1, equalizerSettings.band1Level)
            this.putFloat(BAND_2, equalizerSettings.band1Level)
            this.putFloat(BAND_3, equalizerSettings.band1Level)
            this.putFloat(BAND_4, equalizerSettings.band1Level)
            this.putFloat(BAND_5, equalizerSettings.band1Level)
            this.putFloat(BAND_BASS_BOOST, equalizerSettings.band1Level)
            this.putFloat(BAND_VIRTUALIZER, equalizerSettings.band1Level)
        }.apply()
    }
}