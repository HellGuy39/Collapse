package com.hellguy39.collapse.core.ui.extensions

import com.hellguy39.collapse.core.model.Song
import java.text.SimpleDateFormat


const val MINUTES_PATTERN = "mm:ss"
const val HOUR_PATTERN = "HH:mm:ss"

fun List<Song>.toTotalDateString(): String {
    var totalTime = 0L
    forEach {
        totalTime += it.duration ?: 0
    }
    return totalTime.formatToTimeString()
}

fun Long?.formatToTimeString(): String {
    return if (this == null)
        "00:00"
    else if (this >= 3600 * 1000)
        SimpleDateFormat(HOUR_PATTERN).format(this)
    else
        SimpleDateFormat(MINUTES_PATTERN).format(this)
}