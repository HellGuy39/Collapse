package com.hellguy39.data.repositories

import android.content.SharedPreferences
import com.hellguy39.domain.models.EqualizerSettings
import com.hellguy39.domain.repositories.EqualizerSettingsRepository

private const val IS_EQ_ENABLED = "isEqEnabled"
private const val IS_BASS_ENABLED = "isBassEnabled"
private const val IS_VIRTUALIZER_ENABLED = "isVirtualizerEnabled"
private const val BAND_1 = "band1"
private const val BAND_2 = "band2"
private const val BAND_3 = "band3"
private const val BAND_4 = "band4"
private const val BAND_5 = "band5"
private const val BAND_BASS_BOOST = "band_bass_boost"
private const val BAND_VIRTUALIZER = "band_virtualizer"
private const val PRESET = "preset"

class EqualizerRepositoryImpl(private val prefs: SharedPreferences): EqualizerSettingsRepository {
    override fun getEqualizerSettings(): EqualizerSettings {

        val equalizerSettings = EqualizerSettings(
            isEqEnabled = prefs.getBoolean(IS_EQ_ENABLED, false),
            isBassEnabled = prefs.getBoolean(IS_BASS_ENABLED, false),
            isVirtualizerEnabled = prefs.getBoolean(IS_VIRTUALIZER_ENABLED, false),
            band1Level = prefs.getInt(BAND_1, 0).toShort(),
            band2Level = prefs.getInt(BAND_2, 0).toShort(),
            band3Level = prefs.getInt(BAND_3, 0).toShort(),
            band4Level = prefs.getInt(BAND_4, 0).toShort(),
            band5Level = prefs.getInt(BAND_5, 0).toShort(),
            bandBassBoost = prefs.getInt(BAND_BASS_BOOST, 0).toShort(),
            bandVirtualizer = prefs.getInt(BAND_VIRTUALIZER, 0).toShort(),
            preset = prefs.getInt(PRESET, 0).toShort()
        )

        return equalizerSettings
    }

    override fun saveEqualizerSettings(equalizerSettings: EqualizerSettings) {
        prefs.edit().apply {
            this.putBoolean(IS_EQ_ENABLED, equalizerSettings.isEqEnabled)
            this.putBoolean(IS_VIRTUALIZER_ENABLED, equalizerSettings.isVirtualizerEnabled)
            this.putBoolean(IS_BASS_ENABLED, equalizerSettings.isBassEnabled)
            this.putInt(BAND_1, equalizerSettings.band1Level.toInt())
            this.putInt(BAND_2, equalizerSettings.band2Level.toInt())
            this.putInt(BAND_3, equalizerSettings.band3Level.toInt())
            this.putInt(BAND_4, equalizerSettings.band4Level.toInt())
            this.putInt(BAND_5, equalizerSettings.band5Level.toInt())
            this.putInt(BAND_BASS_BOOST, equalizerSettings.bandBassBoost.toInt())
            this.putInt(BAND_VIRTUALIZER, equalizerSettings.bandVirtualizer.toInt())
            this.putInt(PRESET, equalizerSettings.preset.toInt())
        }.apply()
    }

    override fun savePreset(preset: Short) {
        prefs.edit().apply {
            this.putInt(PRESET, preset.toInt())
        }.apply()
    }

    override fun saveEqSwitch(isEnabled: Boolean) {
        prefs.edit().apply {
            this.putBoolean(IS_EQ_ENABLED, isEnabled)
        }.apply()
    }

    override fun saveBassBoostSwitch(isEnabled: Boolean) {
        prefs.edit().apply {
            this.putBoolean(IS_BASS_ENABLED, isEnabled)
        }.apply()
    }

    override fun saveBassBoostValue(value: Short) {
        prefs.edit().apply {
            this.putInt(BAND_BASS_BOOST, value.toInt())
        }.apply()
    }

    override fun saveVirtualizerValue(value: Short) {
        prefs.edit().apply {
            this.putInt(BAND_VIRTUALIZER, value.toInt())
        }.apply()
    }

    override fun saveVirtualizerSwitch(isEnabled: Boolean) {
        prefs.edit().apply {
            this.putBoolean(IS_VIRTUALIZER_ENABLED, isEnabled)
        }.apply()
    }

    override fun saveBandsLevel(levels: List<Short>) {
        prefs.edit().apply {
            this.putInt(BAND_1, levels[0].toInt())
            this.putInt(BAND_2, levels[1].toInt())
            this.putInt(BAND_3, levels[2].toInt())
            this.putInt(BAND_4, levels[3].toInt())
            this.putInt(BAND_5, levels[4].toInt())
        }.apply()
    }

    override fun saveBandLevel(band: Short, level: Short) {
        when(band) {
            (0).toShort() -> prefs.edit().apply { this.putInt(BAND_1, level.toInt()) }.apply()
            (1).toShort() -> prefs.edit().apply { this.putInt(BAND_2, level.toInt()) }.apply()
            (2).toShort() -> prefs.edit().apply { this.putInt(BAND_3, level.toInt()) }.apply()
            (3).toShort() -> prefs.edit().apply { this.putInt(BAND_4, level.toInt()) }.apply()
            (4).toShort() -> prefs.edit().apply { this.putInt(BAND_5, level.toInt()) }.apply()
        }
    }
}