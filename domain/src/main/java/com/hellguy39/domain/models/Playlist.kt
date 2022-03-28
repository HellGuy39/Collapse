package com.hellguy39.domain.models

import android.os.Parcelable
import com.hellguy39.domain.utils.PlaylistType
import kotlinx.parcelize.Parcelize

@Parcelize
data class Playlist(
    var id: Int = 0,
    var name: String = "",
    var description: String = "",
    var picture: ByteArray? = null,
    var tracks: List<Track> = listOf(),
    var type: Enum<PlaylistType> = PlaylistType.Custom
): Parcelable