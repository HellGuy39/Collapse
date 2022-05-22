package com.hellguy39.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Track(
    var id: Int = 0,
    var name: String = "Unknown",
    var artist: String = "Unknown",
    var duration: Long = 0,
    var path: String = "N/A",
    var genre: String = "N/A",
    var year: Int = 0,
    var mimeType: String = "N/A",
    var displayName: String = "N/A"
) : Parcelable