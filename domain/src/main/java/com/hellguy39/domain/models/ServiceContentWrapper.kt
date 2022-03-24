package com.hellguy39.domain.models

import android.os.Parcelable
import com.hellguy39.domain.utils.PlayerType
import kotlinx.parcelize.Parcelize

@Parcelize
data class ServiceContentWrapper(
    var type: Enum<PlayerType> = PlayerType.Undefined,
    var radioStation: RadioStation? = null,
    var position: Int = 0,
    var playlistName: String? = null,
    var trackList: List<Track>? = null
) : Parcelable
