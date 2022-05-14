package com.hellguy39.collapse.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import java.io.ByteArrayOutputStream

internal fun Bitmap.toByteArray(): ByteArray {
    val compressedBitmap = Bitmap.createScaledBitmap(this, 256, 256, true)
    val stream = ByteArrayOutputStream()
    compressedBitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
    return stream.toByteArray()
}

internal fun ByteArray.toBitmap(): Bitmap {
    return BitmapFactory.decodeByteArray(this,0,this.size)
}

internal fun getImageOfTrackByPath(path: String): Bitmap? {

    if (path.isEmpty())
        return null

    val mmr = MediaMetadataRetriever()
    mmr.setDataSource(path)
    val bytes = mmr.embeddedPicture
    mmr.release()

    return if (bytes != null)
        BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    else
        null
}