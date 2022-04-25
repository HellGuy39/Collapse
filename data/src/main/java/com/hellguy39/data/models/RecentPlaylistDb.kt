package com.hellguy39.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hellguy39.domain.models.Playlist
import com.hellguy39.domain.models.RecentPlaylist
import com.hellguy39.domain.models.Track
import com.hellguy39.domain.utils.PlaylistType

@Entity
data class RecentPlaylistDb(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    var playlistId: Int? = null,
    var name: String = "",
    var description: String = "",
    var picture: ByteArray? = null,
    var type: String = PlaylistType.Undefined.name
) {
    fun toDefaultModel() : RecentPlaylist {
        return RecentPlaylist(
            id = this.playlistId,
            name = this.name,
            description = this.description,
            picture = this.picture,
            type = when(type) {
                PlaylistType.Favourites.name -> PlaylistType.Favourites
                PlaylistType.AllTracks.name -> PlaylistType.AllTracks
                PlaylistType.Artist.name -> PlaylistType.Artist
                PlaylistType.Custom.name -> PlaylistType.Custom
                else -> PlaylistType.Undefined
            }
        )
    }
    fun toDbModel(recentPlaylist: RecentPlaylist): RecentPlaylistDb {
        return RecentPlaylistDb(
            playlistId = recentPlaylist.id,
            name = recentPlaylist.name,
            description = recentPlaylist.description,
            picture = recentPlaylist.picture,
            type = recentPlaylist.type.name
        )
    }
}
