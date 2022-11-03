package com.hellguy39.collapse.songs.util

import android.graphics.Bitmap
import android.net.Uri
import android.util.Size
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
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

fun Fragment.repeatOnLifecycleStarted(block: suspend CoroutineScope.() -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            block()
        }
    }
}

fun Fragment.repeatOnLifecycleResumed(block: suspend CoroutineScope.() -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            block()
        }
    }
}

fun Fragment.repeatOnLifecycleCreated(block: suspend CoroutineScope.() -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            block()
        }
    }
}
