package com.hellguy39.domain.usecases

import android.graphics.Bitmap
import android.graphics.BitmapFactory

class ConvertByteArrayToBitmapUseCase {
    operator fun invoke(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
    }
}