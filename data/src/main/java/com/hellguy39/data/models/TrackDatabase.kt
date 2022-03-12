package com.hellguy39.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hellguy39.domain.models.Track
import java.io.File

@Entity
data class TrackDatabase(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var name: String = "N/A",
    var artist: String = "N/A",
    var path: String = "N/A",
    var embeddedPicture: ByteArray? = null,
) {
    fun toDatabaseEntity(track: Track) {
        this.id = track.id
        this.name = track.name
        this.artist = track.artist
        this.path = track.path
        this.embeddedPicture = track.embeddedPicture
    }

    fun toDefaultModel(): Track {
        return Track(
            id = this.id,
            name = this.name,
            artist = this.artist,
            path = this.path,
            embeddedPicture = this.embeddedPicture,
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TrackDatabase

        if (id != other.id) return false
        if (name != other.name) return false
        if (artist != other.artist) return false
        if (path != other.path) return false
        if (embeddedPicture != null) {
            if (other.embeddedPicture == null) return false
            if (!embeddedPicture.contentEquals(other.embeddedPicture)) return false
        } else if (other.embeddedPicture != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + artist.hashCode()
        result = 31 * result + path.hashCode()
        result = 31 * result + (embeddedPicture?.contentHashCode() ?: 0)
        return result
    }


}