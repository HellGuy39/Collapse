package com.hellguy39.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hellguy39.domain.models.Track

@Entity
data class TrackEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var name: String = "Unknown",
    var artist: String = "Unknown",
    var path: String = "N/A",
    var duration: Long = 0
)