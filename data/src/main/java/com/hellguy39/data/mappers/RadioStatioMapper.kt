package com.hellguy39.data.mappers

import com.hellguy39.data.models.RadioStationEntity
import com.hellguy39.domain.models.RadioStation
import com.hellguy39.domain.utils.Protocol

fun RadioStationEntity.toRadioStation(): RadioStation {
    return RadioStation(
        id = id,
        name = name,
        url = url,
        genre = genre,
        picture = picture,
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

fun RadioStation.toRadioStationEntity(): RadioStationEntity {
    return RadioStationEntity(
        id = id,
        name = name,
        url = url,
        genre = genre,
        picture = picture,
        protocol = protocol.name
    )
}

fun List<RadioStationEntity>.toRadioStationList(): List<RadioStation> {
    val returnableList = mutableListOf<RadioStation>()
    for (n in this.indices) {
        returnableList.add(this[n].toRadioStation())
    }
    return returnableList
}