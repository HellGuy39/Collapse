package com.hellguy39.domain.models

import android.os.Parcelable
import com.hellguy39.domain.utils.PlayerType
import com.hellguy39.domain.utils.PlaylistType
import kotlinx.parcelize.Parcelize

@Parcelize
data class SavedState(
    var playlistId: Int? = null,
    var radioStationId: Int? = null,
    var artistName: String? = null,
    var playerPosition: Long = 0,
    var position: Int = 0,
    var playerType: Enum<PlayerType> = PlayerType.Undefined,
    var playlistType: Enum<PlaylistType> = PlaylistType.Undefined
): Parcelable
