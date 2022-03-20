package com.hellguy39.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Track(
    var id: Int = 0,
    var name: String = "Unknown",
    var artist: String = "Unknown",
    var path: String = "N/A",
    var embeddedPicture: ByteArray? = null,
) : Parcelable