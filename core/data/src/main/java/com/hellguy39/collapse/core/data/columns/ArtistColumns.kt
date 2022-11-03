package com.hellguy39.collapse.core.data.columns

import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.hellguy39.collapse.core.model.Artist

data class ArtistColumns(
    val id: Int,
    val artist: Int,
    val numberOfTracks: Int,
    val numberOfAlbums: Int
) {
    fun getArtist(cursor: Cursor): Artist {
        val id = cursor.getLong(id)
        val contentUri: Uri = ContentUris.withAppendedId(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            id
        )
        return Artist(
            id = id,
            contentUri = contentUri,
            artist = cursor.getString(artist),
            numberOfAlbums = cursor.getInt(numberOfAlbums),
            numberOfTracks = cursor.getInt(numberOfTracks)
        )
    }
}

fun Cursor.getArtistColumns(): ArtistColumns = ArtistColumns(
    id = getColumnIndexOrThrow(MediaStore.Audio.Artists._ID),
    artist = getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST),
    numberOfTracks = getColumnIndexOrThrow(MediaStore.Audio.Artists.NUMBER_OF_TRACKS),
    numberOfAlbums = getColumnIndexOrThrow(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS)
)
