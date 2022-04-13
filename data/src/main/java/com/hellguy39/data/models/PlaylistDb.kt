package com.hellguy39.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hellguy39.domain.models.Playlist
import com.hellguy39.domain.models.Track
import com.hellguy39.domain.utils.PlaylistType

@Entity
data class PlaylistDb(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    var name: String = "",
    var description: String = "",
    var picture: ByteArray? = null,
    var tracks: MutableList<Track> = mutableListOf(),
    var type: String = PlaylistType.Custom.name
) {
    fun toDefaultModel() : Playlist {
        return Playlist(
            id = this.id,
            name = this.name,
            description = this.description,
            picture = this.picture,
            tracks = this.tracks
        )
    }
    fun toDbModel(playlist: Playlist): PlaylistDb {
        return PlaylistDb(
            id = playlist.id,
            name = playlist.name,
            description = playlist.description,
            picture = playlist.picture,
            tracks = playlist.tracks
        )
    }
}
