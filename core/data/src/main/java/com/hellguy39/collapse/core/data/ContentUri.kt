package com.hellguy39.collapse.core.data

import android.net.Uri
import android.os.Build
import android.provider.MediaStore

object ContentUri {

    val externalArtists: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
        MediaStore.Audio.Artists.getContentUri(MediaStore.VOLUME_EXTERNAL)
    else
        MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI

    val externalSongs: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
        MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
    else
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

    val externalAlbums: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
        MediaStore.Audio.Albums.getContentUri(MediaStore.VOLUME_EXTERNAL)
    else
        MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI

}