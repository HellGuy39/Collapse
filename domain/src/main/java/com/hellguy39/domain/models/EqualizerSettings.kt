package com.hellguy39.domain.models

data class EqualizerSettings(
    var isEnabled: Boolean = false,
    var band1Level: Float = 0f,
    var band2Level: Float = 0f,
    var band3Level: Float = 0f,
    var band4Level: Float = 0f,
    var band5Level: Float = 0f,
    var preset: Int = 0,
    var bandBassBoost: Float = 0f,
    var bandVirtualizer: Float = 0f
)
