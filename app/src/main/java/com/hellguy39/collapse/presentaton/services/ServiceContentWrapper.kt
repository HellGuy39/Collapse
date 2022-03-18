package com.hellguy39.collapse.presentaton.services

import com.hellguy39.collapse.utils.PlayerType
import com.hellguy39.domain.models.Track
import java.io.Serializable

data class ServiceContentWrapper(
    var type: Enum<PlayerType> = PlayerType.Undefined,
    var url: String? = null,
    var position: Int = 0,
    var trackList: List<Track>? = null
) : Serializable
