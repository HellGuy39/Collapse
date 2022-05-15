package com.hellguy39.data.mappers

import com.hellguy39.data.models.TrackEntity
import com.hellguy39.domain.models.Playlist
import com.hellguy39.domain.models.Track

fun TrackEntity.toTrack(): Track {
    return Track(
        id = id,
        name = name,
        artist = artist,
        path = path,
        duration = duration
    )
}

fun Track.toTrackEntity(): TrackEntity {
    return TrackEntity(
        id = id,
        name = name,
        artist = artist,
        path = path,
        duration = duration
    )
}

fun List<TrackEntity>.toTrackList(): List<Track> {
    val returnableList = mutableListOf<Track>()

    for (n in this.indices) {
        returnableList.add(this[n].toTrack())
    }

    return returnableList
}

fun Track.isSameTrackOf(trackEntity: TrackEntity): Boolean {
    return (trackEntity.path == path)
}

fun TrackEntity.isSameTrackOf(track: Track): Boolean {
    return (track.path == path)
}

