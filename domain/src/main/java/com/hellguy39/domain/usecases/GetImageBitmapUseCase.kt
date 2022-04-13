package com.hellguy39.domain.usecases

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever

class GetImageBitmapUseCase {

   private val mmr = MediaMetadataRetriever()

   operator fun invoke(path: String): Bitmap? {

        if (path.isEmpty())
            return null

        mmr.setDataSource(path)

        val bytes = mmr.embeddedPicture

        return if (bytes != null)
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        else
            null
    }
}