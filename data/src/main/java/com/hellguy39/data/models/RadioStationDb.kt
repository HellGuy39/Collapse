package com.hellguy39.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hellguy39.domain.models.RadioStation
import com.hellguy39.domain.utils.Protocol

@Entity
data class RadioStationDb(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var name: String = "Unknown",
    var url: String? = null,
    var genre: String? = null,
    var protocol: String = Protocol.HLS.name
) {
    fun toDbModel(radioStation: RadioStation) : RadioStationDb{
        return RadioStationDb(
            id = radioStation.id,
            name = radioStation.name,
            url = radioStation.url,
            genre = radioStation.genre,
            protocol = radioStation.protocol.name
        )
    }

    fun toDefaultModel(): RadioStation {
        return RadioStation(
            id = this.id,
            name = this.name,
            url = this.url,
            genre = this.genre,
            protocol = when(protocol) {
                Protocol.HLS.name -> Protocol.HLS
                Protocol.DASH.name -> Protocol.DASH
                Protocol.Progressive.name -> Protocol.Progressive
                Protocol.RTSP.name -> Protocol.RTSP
                Protocol.SmoothStreaming.name -> Protocol.SmoothStreaming
                else -> Protocol.HLS
            }
        )
    }
}
