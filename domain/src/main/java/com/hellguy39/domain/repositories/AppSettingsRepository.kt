package com.hellguy39.domain.repositories

import com.hellguy39.domain.models.AppSettings

interface AppSettingsRepository {
    fun getSettings(): AppSettings
    fun saveIsAnimationsEnabled(enabled: Boolean)
    fun saveIsSaveStateEnabled(enabled: Boolean)
}