package com.hellguy39.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hellguy39.domain.models.Track

@Entity
data class TrackDatabase(
    @PrimaryKey var id: Int = 0,
    var name: String = "N/A",
    var author: String = "N/A",
    var path: String = "N/A"
) {
    fun toDatabaseEntity(track: Track) {
        this.id = track.id
        this.name = track.name
        this.author = track.author
        this.path = track.path
    }

    fun toDefaultModel(): Track {
        return Track(
            id = this.id,
            name = this.name,
            author = this.author,
            path = this.path
        )
    }
}