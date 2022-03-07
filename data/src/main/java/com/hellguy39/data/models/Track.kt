package com.hellguy39.data.models

import android.graphics.Bitmap

data class Track(
    var name: String = "N/A",
    var author: String = "N/A",
    var image: Bitmap? = null,
    var path: String = "N/A"
)
