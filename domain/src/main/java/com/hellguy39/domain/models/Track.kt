package com.hellguy39.domain.models

data class Track(
    val id: Int,
    var name: String = "N/A",
    var author: String = "N/A",
    var path: String = "N/A"
)