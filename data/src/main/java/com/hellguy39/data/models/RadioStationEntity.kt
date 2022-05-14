package com.hellguy39.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hellguy39.domain.models.RadioStation
import com.hellguy39.domain.utils.Protocol

@Entity
data class RadioStationEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var name: String = "Unknown",
    var url: String? = null,
    var genre: String? = null,
    var picture: ByteArray? = null,
    var protocol: String = Protocol.HLS.name
)
