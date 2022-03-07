package com.hellguy39.data.models

import android.graphics.Bitmap
import androidx.room.Entity

@Entity
data class Playlist(
    var tittle: String = "N/A",
    var image: Bitmap? = null,
    //var tracks: List<Track> = listOf()
)
