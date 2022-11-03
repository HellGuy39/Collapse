package com.hellguy39.collapse.core.data.columns

import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.hellguy39.collapse.core.model.Song

data class SongColumns(
    val id: Int,
    val artistId: Int,
    val albumId: Int,
    val displayName: Int,
    val bucketDisplayName: Int,
    val duration: Int,
    val size: Int,
    val bitrate: Int,
    val title: Int,
    val composer: Int,
    val artist: Int
) {
    fun getSong(cursor: Cursor): Song {
        val id = cursor.getLong(id)
        val contentUri: Uri = ContentUris.withAppendedId(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            id
        )
        return Song(
            id = id,
            artistId = cursor.getLong(artistId),
            albumId = cursor.getLong(albumId),
            contentUri = contentUri,
            displayName = cursor.getString(displayName),
            bucketDisplayName = cursor.getString(bucketDisplayName),
            size = cursor.getInt(size),
            bitrate = cursor.getInt(bitrate),
            composer = cursor.getString(composer),
            duration = cursor.getLong(duration),
            title = cursor.getString(title),
            artist = cursor.getString(artist)
        )
    }
}

fun Cursor.getSongColumns(): SongColumns = SongColumns(
    id = getColumnIndexOrThrow(MediaStore.Audio.Media._ID),
    artistId = getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID),
    albumId = getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID),
    displayName = getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME),
    bucketDisplayName = getColumnIndexOrThrow(MediaStore.Audio.Media.BUCKET_DISPLAY_NAME),
    duration = getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION),
    size = getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE),
    bitrate = getColumnIndexOrThrow(MediaStore.Audio.Media.BITRATE),
    title = getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE),
    composer = getColumnIndexOrThrow(MediaStore.Audio.Media.COMPOSER),
    artist = getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST),
)
