package com.hellguy39.collapse.core.data.columns

import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.hellguy39.collapse.core.model.Album
import com.hellguy39.collapse.core.model.Artist

data class AlbumColumns(
    val id: Int,
    val artist: Int,
    val album: Int
) {
    fun getAlbum(cursor: Cursor): Album {
        val id = cursor.getLong(id)
        val contentUri: Uri = ContentUris.withAppendedId(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            id
        )
        return Album(
            id = id,
            contentUri = contentUri,
            album = cursor.getString(album),
            artist = cursor.getString(artist)
        )
    }
}

fun Cursor.getAlbumColumns(): AlbumColumns = AlbumColumns(
    id = getColumnIndexOrThrow(MediaStore.Audio.Albums._ID),
    artist = getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST),
    album = getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM)
)
