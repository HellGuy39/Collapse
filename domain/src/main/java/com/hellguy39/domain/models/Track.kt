package com.hellguy39.domain.models

import java.io.Serializable

data class Track(
    var id: Int = 0,
    var name: String = "Unknown",
    var artist: String = "Unknown",
    var path: String = "N/A",
    var embeddedPicture: ByteArray? = null,
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Track

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