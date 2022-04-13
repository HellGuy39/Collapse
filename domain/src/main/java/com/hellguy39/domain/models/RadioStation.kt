package com.hellguy39.domain.models

import android.os.Parcelable
import com.hellguy39.domain.utils.Protocol
import kotlinx.parcelize.Parcelize

@Parcelize
data class RadioStation(
    var id: Int = 0,
    var name: String = "Unknown",
    var picture: ByteArray? = null,
    var url: String? = null,
    var genre: String? = null,
    var protocol: Enum<Protocol> = Protocol.HLS
) : Parcelable
