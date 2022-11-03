package com.hellguy39.collapse.core.model

import android.net.Uri

data class Song(
    val id: Long?,
    val artistId: Long?,
    val albumId: Long?,
    val contentUri: Uri?,
    val displayName: String?,
    val bucketDisplayName: String?,
    val duration: Long?,
    val size: Int?,
    val bitrate: Int?,
    val title: String?,
    val composer: String?,
    val artist: String?
)
