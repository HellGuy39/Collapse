package com.hellguy39.data.repositories

import android.content.SharedPreferences
import com.hellguy39.domain.models.AppSettings
import com.hellguy39.domain.repositories.AppSettingsRepository
import com.hellguy39.domain.utils.LayoutType

private const val IS_ANIMATIONS_ENABLED = "is_animations_enabled"
private const val IS_SAVE_STATE_ENABLED = "is_save_state_enabled"
private const val IS_ADAPTABLE_BACKGROUND_ENABLED = "is_adaptable_background_enabled"
private const val LAYOUT_TYPE = "layout_type"

class AppSettingsRepositoryImpl(private val prefs: SharedPreferences): AppSettingsRepository {
    override fun getSettings(): AppSettings {
        return AppSettings(
            isAnimationsEnabled = prefs.getBoolean(IS_ANIMATIONS_ENABLED, true),
            isSaveStateEnabled = prefs.getBoolean(IS_SAVE_STATE_ENABLED, true),
            isAdaptableBackgroundEnabled = prefs.getBoolean(IS_ADAPTABLE_BACKGROUND_ENABLED, false)
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

    override fun saveIsAdaptableBackgroundEnabled(enabled: Boolean) {
        prefs.edit().apply {
            this.putBoolean(IS_ADAPTABLE_BACKGROUND_ENABLED, enabled)
        }.apply()
    }

    override fun getLayoutType(): LayoutType {
        return when(prefs.getString(LAYOUT_TYPE, LayoutType.Grid.name)) {
            LayoutType.Grid.name -> LayoutType.Grid
            LayoutType.Linear.name -> LayoutType.Linear
            else -> LayoutType.Grid
        }
    }

    override fun saveLayoutType(type: LayoutType) {
        prefs.edit().apply {
            this.putString(LAYOUT_TYPE, type.name)
        }.apply()
    }

}