package com.hellguy39.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Artist(
    var name: String = "Unknown",
    var trackList: MutableList<Track> = mutableListOf()
) : Parcelable
