package com.hellguy39.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hellguy39.domain.models.Track

@Entity
data class TrackDb(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var name: String = "Unknown",
    var artist: String = "Unknown",
    var path: String = "N/A",
) {
    fun toDbModel(track: Track) : TrackDb{
        return TrackDb(
            id = track.id,
            name = track.name,
            artist = track.artist,
            path = track.path
        )
    }

    fun toDefaultModel(): Track {
        return Track(
            id = this.id,
            name = this.name,
            artist = this.artist,
            path = this.path
        )
    }
}
