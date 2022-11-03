package com.hellguy39.collapse.core.ui.extensions

import android.content.res.Resources
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
            block()
        }
    }
}

fun AppCompatActivity.repeatOnLifecycleStarted(block: suspend CoroutineScope.() -> Unit) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            block()
        }
    }
}

fun AppCompatActivity.repeatOnLifecycleResumed(block: suspend CoroutineScope.() -> Unit) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.RESUMED) {
            block()
        }
    }
}

fun AppCompatActivity.repeatOnLifecycleCreated(block: suspend CoroutineScope.() -> Unit) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.CREATED) {
            block()
        }
    }
}

fun Resources.Theme.getColorByResId(resId: Int): Int {
    val typedValue = TypedValue()
    this.resolveAttribute(resId, typedValue, true)
    return typedValue.data
}

