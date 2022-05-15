package com.hellguy39.data.repositories

import android.content.SharedPreferences
import com.hellguy39.domain.repositories.VirtualizerRepository

private const val IS_VIRTUALIZER_ENABLED = "is_virtualizer_enabled"
private const val VIRTUALIZER_STRENGTH = "virtualizer_strength"

class VirtualizerRepositoryImpl(private val prefs: SharedPreferences): VirtualizerRepository {
    override fun getStrength(): Short {
        return prefs.getInt(VIRTUALIZER_STRENGTH, 0).toShort()
    }

    override fun saveStrength(strength: Short) {
        prefs.edit().apply {
            putInt(VIRTUALIZER_STRENGTH, strength.toInt())
        }.apply()
    }

    override fun getIsEnabled(): Boolean {
        return prefs.getBoolean(IS_VIRTUALIZER_ENABLED, false)
    }

    override fun saveIsEnabled(enabled: Boolean) {
        prefs.edit().apply {
            putBoolean(IS_VIRTUALIZER_ENABLED, enabled)
        }.apply()
    }

}