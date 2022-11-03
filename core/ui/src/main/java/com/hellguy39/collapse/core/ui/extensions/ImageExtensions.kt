package com.hellguy39.collapse.core.ui.extensions

import android.graphics.Bitmap
import android.net.Uri
import android.util.Size
import android.widget.ImageView
import androidx.annotation.IdRes
import com.bumptech.glide.Glide
import com.hellguy39.collapse.core.ui.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.FileNotFoundException

fun ImageView.setImageAsync(uri: Uri?) {

    if (uri == null)
        return

    val sizeExtraSmall = Size(480, 320)
    val sizeSmall = Size(640, 480)
    val sizeMedium = Size(1280, 720)

    CoroutineScope(Dispatchers.IO).launch {

        try {
            val thumbnail: Bitmap = this@setImageAsync.context.contentResolver
                .loadThumbnail(uri, sizeExtraSmall, null)

            withContext(Dispatchers.Main) {
                Glide.with(this@setImageAsync.context)
                    .load(thumbnail)
                    .into(this@setImageAsync)
            }
        } catch (e: FileNotFoundException) {

        }
    }
}

fun ImageView.setArtistImage() {
    Glide.with(this.context)
        .load(R.drawable.ic_baseline_person_24)
        .into(this)
}

fun ImageView.setImageTest() {
    Glide.with(this.context)
        .load(R.drawable.test_artist_image)
        .into(this)
}