package com.hellguy39.data.models

import android.graphics.Bitmap

data class Playlist(
    var tittle: String = "N/A",
    var image: Bitmap? = null,
    var path: String = "N/A"
)
