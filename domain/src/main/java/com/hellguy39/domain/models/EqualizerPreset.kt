package com.hellguy39.domain.models

data class EqualizerPreset(
    var name: String = "N/A",
    var presetNumber: Short = -1,
    var bandValues: MutableList<Short> = mutableListOf()
)