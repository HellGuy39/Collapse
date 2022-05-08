package com.hellguy39.domain.models

data class EqualizerPreset(
    var name: String = "N/A",
    var presetNumber: Short = -1,
    var band1Level: Short = 0,
    var band2Level: Short = 0,
    var band3Level: Short = 0,
    var band4Level: Short = 0,
    var band5Level: Short = 0,
)