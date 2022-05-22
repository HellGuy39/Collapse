package com.hellguy39.domain.repositories

import com.hellguy39.domain.models.AppSettings
import com.hellguy39.domain.utils.LayoutType

interface AppSettingsRepository {
    fun getSettings(): AppSettings
    fun saveIsAnimationsEnabled(enabled: Boolean)
    fun saveIsSaveStateEnabled(enabled: Boolean)
    fun saveIsAdaptableBackgroundEnabled(enabled: Boolean)
    fun getLayoutType(): LayoutType
    fun saveLayoutType(type: LayoutType)
}