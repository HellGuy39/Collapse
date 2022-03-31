package com.hellguy39.domain.usecases

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream

class ConvertBitmapToByteArrayUseCase {
    operator fun invoke(bitmap: Bitmap?): ByteArray? {

        if (bitmap == null)
            return null

        val compressedBitmap = Bitmap.createScaledBitmap(bitmap, 256, 256, true)

        val stream = ByteArrayOutputStream()
        compressedBitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
        return stream.toByteArray()
    }
}