package com.hellguy39.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RadioStation(
    var name: String = "Unknown",
    var url: String? = null,
    var picture: ByteArray? = null,
    var genre: String? = null
) : Parcelable
