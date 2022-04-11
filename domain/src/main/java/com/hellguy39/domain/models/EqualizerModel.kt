package com.hellguy39.domain.models

import android.media.audiofx.Equalizer

data class EqualizerModel(
    var equalizer: Equalizer? = null,
    var numberOfPresets: Short = 0,
    var presetNames: MutableList<String> = mutableListOf(),
    var lowestBandLevel: Short = -1500,
    var upperBandLevel: Short = 1500,
    var numberOfBands: Short = 5,
    var bandsCenterFreq: ArrayList<Int> = ArrayList(0)
)
