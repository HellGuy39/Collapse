package com.hellguy39.collapse.utils

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import android.util.Size
import android.widget.ImageView
import com.bumptech.glide.Glide
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException

internal fun Bitmap.toByteArray(): ByteArray {
    val compressedBitmap = Bitmap.createScaledBitmap(this, 256, 256, true)
    val stream = ByteArrayOutputStream()
    compressedBitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
    return stream.toByteArray()
}

internal fun ByteArray?.toBitmap(): Bitmap? {
    if (this == null) return null
    return BitmapFactory.decodeByteArray(this,0,this.size)
}

internal fun getImageOfTrackByPath(path: String): Bitmap? {

    if (path.isEmpty() || path.isBlank())
        return null

    val mmr = MediaMetadataRetriever()
    val bytes: ByteArray?

    try {
        mmr.setDataSource(path)
        bytes = mmr.embeddedPicture
    } catch (e: Exception) {
        Log.d("Error", e.printStackTrace().toString())
        return null
    }
    mmr.release()

    return if (bytes != null)
        BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
//            .also {
//            Log.d("DEBUG", "WIDTH: ${it.width}\tHEIGHT: ${it.height}")
//        }
    else
        null
}
