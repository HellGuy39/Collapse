package com.hellguy39.collapse.core.data

import android.provider.MediaStore

object Projections {

    val artistProjection = arrayOf(
        MediaStore.Audio.Artists._ID,
        MediaStore.Audio.Artists.ARTIST,
        MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
        MediaStore.Audio.Artists.NUMBER_OF_TRACKS
    )

    val albumProjection = arrayOf(
        MediaStore.Audio.Albums._ID,
        MediaStore.Audio.Albums.ALBUM,
        MediaStore.Audio.Albums.ARTIST
    )

    val songProjection = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.ARTIST_ID,
        MediaStore.Audio.Media.ALBUM_ID,
        MediaStore.Audio.Media.BUCKET_DISPLAY_NAME,
        MediaStore.Audio.Media.DISPLAY_NAME,
        MediaStore.Audio.Media.DURATION,
        MediaStore.Audio.Media.SIZE,
        MediaStore.Audio.Media.BITRATE,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.COMPOSER,
        MediaStore.Audio.Media.ARTIST
    )


}