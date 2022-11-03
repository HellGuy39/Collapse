package com.hellguy39.collapse.core.model

import android.net.Uri

data class Album(
    val id: Long?,
    val contentUri: Uri?,
    val album: String?,
    val artist: String?
)
