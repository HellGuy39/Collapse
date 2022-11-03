package com.hellguy39.collapse.core.model

import android.net.Uri

data class Artist(
    val id: Long?,
    val contentUri: Uri?,
    val artist: String?,
    val numberOfAlbums: Int?,
    val numberOfTracks: Int?
)
