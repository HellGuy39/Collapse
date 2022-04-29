package com.hellguy39.domain.models

data class EqualizerSettings(
    var isEqEnabled: Boolean = false,
    var band1Level: Short = 0,
    var band2Level: Short = 0,
    var band3Level: Short = 0,
    var band4Level: Short = 0,
    var band5Level: Short = 0,
    var preset: Short = 0,

    var bandVirtualizer: Short = 0,
    var isVirtualizerEnabled: Boolean = false,

    var isBassEnabled: Boolean = false,
    var bandBassBoost: Short = 0,

    var isReverbEnabled: Boolean = false,
    var reverbPreset: Short = 0 // Preset none
)
