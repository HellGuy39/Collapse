package com.hellguy39.data.repositories

import android.content.SharedPreferences
import com.hellguy39.domain.models.AppSettings
import com.hellguy39.domain.repositories.AppSettingsRepository

private const val IS_ANIMATIONS_ENABLED = "is_animations_enabled"
private const val IS_SAVE_STATE_ENABLED = "is_save_state_enabled"

class AppSettingsRepositoryImpl(private val prefs: SharedPreferences): AppSettingsRepository {
    override fun getSettings(): AppSettings {
        return AppSettings(
            isAnimationsEnabled = prefs.getBoolean(IS_ANIMATIONS_ENABLED, true),
            isSaveStateEnabled = prefs.getBoolean(IS_SAVE_STATE_ENABLED, true)
        )
    }

    override fun saveIsAnimationsEnabled(enabled: Boolean) {
        prefs.edit().apply {
            this.putBoolean(IS_ANIMATIONS_ENABLED, enabled)
        }.apply()
    }

    override fun saveIsSaveStateEnabled(enabled: Boolean) {
        prefs.edit().apply {
            this.putBoolean(IS_SAVE_STATE_ENABLED, enabled)
        }.apply()
    }
}