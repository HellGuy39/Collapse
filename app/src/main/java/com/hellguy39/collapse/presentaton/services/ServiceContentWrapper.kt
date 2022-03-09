package com.hellguy39.collapse.presentaton.services

import com.hellguy39.domain.models.Track
import java.io.Serializable

data class ServiceContentWrapper(
    var position: Int = 0,
    var trackList: List<Track>
) : Serializable
