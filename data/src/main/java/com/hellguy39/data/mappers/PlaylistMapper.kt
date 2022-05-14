package com.hellguy39.data.mappers

import com.hellguy39.data.models.PlaylistEntity
import com.hellguy39.data.repositories.TracksRepositoryImpl.Companion.ITEM_NOT_CONTAINS
import com.hellguy39.domain.models.Playlist
import com.hellguy39.domain.utils.PlaylistType

fun PlaylistEntity.toPlaylist(): Playlist {
    return Playlist(
        id = id,
        name = name,
        description = description,
        picture = picture,
        tracks = tracks,
        type = when(type) {
            PlaylistType.Favourites.name -> PlaylistType.Favourites
            PlaylistType.AllTracks.name -> PlaylistType.AllTracks
            PlaylistType.Artist.name -> PlaylistType.Artist
            PlaylistType.Custom.name -> PlaylistType.Custom
            else -> PlaylistType.Undefined
        }
    )
}

fun Playlist.toPlaylistEntity(): PlaylistEntity {
    return PlaylistEntity(
        id = id,
        name = name,
        description = description,
        picture = picture,
        tracks = tracks,
        type = type.name
    )
}

fun List<PlaylistEntity>.toPlaylistList(): List<Playlist> {
    val returnableList = mutableListOf<Playlist>()
    for (n in this.indices) {
        returnableList.add(this[n].toPlaylist())
    }
    return returnableList
}

fun List<Playlist>.isContainArtist(artist: String): Int {
    for (n in this.indices) {
        if (this[n].name == artist) {
            return n
        }
    }
    return ITEM_NOT_CONTAINS
}
