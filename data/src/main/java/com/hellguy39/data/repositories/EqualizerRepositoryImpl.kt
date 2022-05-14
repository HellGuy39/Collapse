package com.hellguy39.data.repositories

import android.content.SharedPreferences
import com.hellguy39.domain.repositories.EqualizerRepository

private const val IS_EQ_ENABLED = "is_eq_enabled"
private const val EQ_PRESET_NUMBER = "eq_preset_number"
private const val EQ_BAND_1_LEVEL = "eq_band_1_level"
private const val EQ_BAND_2_LEVEL = "eq_band_2_level"
private const val EQ_BAND_3_LEVEL = "eq_band_3_level"
private const val EQ_BAND_4_LEVEL = "eq_band_4_level"
private const val EQ_BAND_5_LEVEL = "eq_band_5_level"

class EqualizerRepositoryImpl(private val prefs: SharedPreferences): EqualizerRepository {
    override fun savePreset(values: MutableList<Short>) {
        prefs.edit().apply {
            putInt(EQ_BAND_1_LEVEL, values[0].toInt())
            putInt(EQ_BAND_2_LEVEL, values[1].toInt())
            putInt(EQ_BAND_3_LEVEL, values[2].toInt())
            putInt(EQ_BAND_4_LEVEL, values[3].toInt())
            putInt(EQ_BAND_5_LEVEL, values[4].toInt())
        }.apply()
    }

    override fun getPreset(): MutableList<Short> {
        return mutableListOf(
            prefs.getInt(EQ_BAND_1_LEVEL, 0).toShort(),
            prefs.getInt(EQ_BAND_2_LEVEL, 0).toShort(),
            prefs.getInt(EQ_BAND_3_LEVEL, 0).toShort(),
            prefs.getInt(EQ_BAND_4_LEVEL, 0).toShort(),
            prefs.getInt(EQ_BAND_5_LEVEL, 0).toShort()
        )
    }

    override fun getPresetNumber(): Short {
        return prefs.getInt(EQ_PRESET_NUMBER, -1).toShort()
    }

    override fun savePresetNumber(preset: Short) {
        prefs.edit().apply {
            putInt(EQ_PRESET_NUMBER, preset.toInt())
        }.apply()
    }

    override fun getIsEnabled(): Boolean {
        return prefs.getBoolean(IS_EQ_ENABLED, false)
    }

    override fun saveIsEnabled(enabled: Boolean) {
        prefs.edit().apply {
            putBoolean(IS_EQ_ENABLED, enabled)
        }.apply()
    }
}