package com.hellguy39.domain.models

data class RadioStation(
    var name: String = "Unknown",
    var url: String? = null,
    var picture: ByteArray? = null,
    var genre: String? = null
)