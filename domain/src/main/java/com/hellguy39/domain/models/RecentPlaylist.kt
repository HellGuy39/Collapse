package com.hellguy39.domain.models

import com.hellguy39.domain.utils.PlaylistType

data class RecentPlaylist(
    var id: Int? = null,
    var playlistId: Int? = null,
    var name: String = "",
    var description: String = "",
    var picture: ByteArray? = null,
    var type: Enum<PlaylistType> = PlaylistType.Undefined
)