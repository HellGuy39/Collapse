package com.hellguy39.domain.models

data class Recording(
    var name: String = "Unknown",
    var artist: String = "Unknown",
    var duration: Long = 0,
    var path: String = "N/A",
    var genre: String = "N/A",
    var year: Int = 0
)
