package com.hellguy39.collapse.utils

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.drawable.InsetDrawable
import android.util.TypedValue
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.transition.TransitionManager
import com.google.android.material.transition.MaterialSharedAxis
import java.text.SimpleDateFormat
import java.util.*

private const val PATTERN_1 = "m:ss"
private const val PATTERN_2 = "mm:ss"
private const val PATTERN_3 = "H:mm:ss"
private const val PATTERN_4 = "HH:mm:ss"
private const val TEN_MINUTES: Long = 600000
private const val ONE_HOUR: Long = 3600000
private const val TEN_HOURS: Long = 36000000

internal fun Long.formatAsDate(): String {
    return when (this) {
        in 0..TEN_MINUTES -> SimpleDateFormat(PATTERN_1, Locale.getDefault()).format(Date(this))
        in TEN_MINUTES..ONE_HOUR -> SimpleDateFormat(PATTERN_2, Locale.getDefault()).format(Date(this))
        in ONE_HOUR..TEN_HOURS -> SimpleDateFormat(PATTERN_3, Locale.getDefault()).format(Date(this))
        else -> SimpleDateFormat(PATTERN_4, Locale.getDefault()).format(Date(this))
    }
}

internal fun Fragment.isPermissionGranted(
    permission: String
): Boolean = (ActivityCompat.checkSelfPermission(
        requireActivity(),
        permission
    ) == PackageManager.PERMISSION_GRANTED)

internal fun CharSequence?.formatForDisplaying(): CharSequence {
    return if(this.isNullOrBlank())
        "Unknown"
    else if(this.isNullOrEmpty())
        "Unknown"
    else
        this
}

internal fun Short.toSliderValue(): Float {
    return (this / 100).toFloat()
}

internal fun Float.toAdjustableValue(): Short {
    return (this * 100).toInt().toShort()
}

//internal fun List<Track>.getTotalDuration(): Long {
//    var totalDuration: Long = 0
//
//    for (track in this)
//        totalDuration += track.duration
//
//    return totalDuration
//}

internal fun View.updateAnimation(container: ViewGroup) {
    TransitionManager.beginDelayedTransition(
        container,
        MaterialSharedAxis(MaterialSharedAxis.Y, true).apply {
            duration = 300L
        }
    )
    this.visibility = View.GONE
    this.visibility = View.VISIBLE
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