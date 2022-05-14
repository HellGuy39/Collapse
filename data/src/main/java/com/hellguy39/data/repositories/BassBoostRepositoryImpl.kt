package com.hellguy39.data.repositories

import android.content.SharedPreferences
import com.hellguy39.domain.repositories.BassBoostRepository

private const val IS_BASS_BOOST_ENABLED = "is_bass_boost_enabled"
private const val BASS_STRENGTH = "bass_strength"

class BassBoostRepositoryImpl(private val prefs: SharedPreferences): BassBoostRepository {
    override fun getStrength(): Short {
        return prefs.getInt(BASS_STRENGTH, 0).toShort()
    }

    override fun saveStrength(strength: Short) {
        prefs.edit().apply {
            putInt(BASS_STRENGTH, strength.toInt())
        }.apply()
    }

    override fun getIsEnabled(): Boolean {
        return prefs.getBoolean(IS_BASS_BOOST_ENABLED, false)
    }

    override fun saveIsEnabled(enabled: Boolean) {
        prefs.edit().apply {
            putBoolean(IS_BASS_BOOST_ENABLED, enabled)
        }.apply()
    }

}