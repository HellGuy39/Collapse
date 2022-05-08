package com.hellguy39.domain.models

data class EqualizerProperties(
    var bassBoostSupport: Boolean = false,
    var virtualizerSupport: Boolean = false,
    var numberOfPresets: Short = 0,
    var presets: MutableList<EqualizerPreset>,
    var lowestBandLevel: Short = -1500,
    var upperBandLevel: Short = 1500,
    var numberOfBands: Short = 5,
    var bandsCenterFreq: ArrayList<Int> = ArrayList(0)
)
