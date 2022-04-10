package com.hellguy39.domain.models

import android.os.Parcelable
import com.hellguy39.domain.utils.PlayerType
import kotlinx.parcelize.Parcelize

@Parcelize
data class ServiceContentWrapper(
    var type: Enum<PlayerType> = PlayerType.Undefined,

    var radioStation: RadioStation? = null,
    var playlist: Playlist? = null,
    var artist: Artist? = null,

    var playerPosition: Long = 0,
    var position: Int = 0,
    var fromSavedState: Boolean = false
) : Parcelable
