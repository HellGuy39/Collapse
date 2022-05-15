package com.hellguy39.collapse.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.InsetDrawable
import android.util.TypedValue
import android.view.Menu
import androidx.appcompat.view.menu.MenuBuilder
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.android.material.transition.platform.MaterialFadeThrough
import com.hellguy39.collapse.R
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

private const val PATTERN = "m:ss"

internal fun Long.formatAsDate(): String {
    return SimpleDateFormat(PATTERN, Locale.getDefault()).format(Date(this))
}

internal fun Short.toSliderValue(): Float {
    return (this / 100).toFloat()
}

internal fun Float.toAdjustableValue(): Short {
    return (this * 100).toInt().toShort()
}

internal fun Int.formatAsFreq(): String {
    return if (this > 1000)
        "${this.toDouble() / 1000} kHz"
    else
        "$this Hz"
}

internal fun Resources.Theme.getColorByResId(resId: Int): Int {
    val typedValue = TypedValue()
    this.resolveAttribute(resId, typedValue, true)
    return typedValue.data
}

@SuppressLint("RestrictedApi")
internal fun Menu.setupIcons(resources: Resources) {
    val menuBuilder = this as MenuBuilder
    menuBuilder.setOptionalIconsVisible(true)
    for (item in menuBuilder.visibleItems) {
        val iconMarginPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 4f, resources.displayMetrics).toInt()
        if (item.icon != null) {
            item.icon = InsetDrawable(item.icon, iconMarginPx, 0, iconMarginPx,0)
        }
    }
}