package com.hellguy39.collapse.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
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
import com.google.android.material.transition.platform.MaterialFade
import com.google.android.material.transition.platform.MaterialFadeThrough
import com.hellguy39.collapse.R

internal fun MaterialToolbar.setOnBackFragmentNavigation(navController: NavController) {
    this.setNavigationOnClickListener {
        navController.popBackStack()
    }
}

internal fun MaterialToolbar.setOnBackActivityNavigation(activity: Activity) {
    this.setNavigationOnClickListener {
        activity.onBackPressed()
    }
}

internal fun getVerticalLayoutManager(context: Context?): LinearLayoutManager {
    return LinearLayoutManager(
        context,
        LinearLayoutManager.VERTICAL,
        false
    )
}

internal fun getGridLayoutManager(context: Context?): GridLayoutManager {
    return GridLayoutManager(
        context,
        2,
        GridLayoutManager.VERTICAL,
        false
    )
}

internal fun Fragment.setMaterialFadeThoughtAnimations() {
    enterTransition = MaterialFadeThrough()
    reenterTransition = MaterialFadeThrough()
    //exitTransition = MaterialFade()
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

internal fun RecyclerView.getTrackItemVerticalDivider(context: Context): MaterialDividerItemDecoration {
    val divider = MaterialDividerItemDecoration(context, LinearLayoutManager.VERTICAL)
    divider.setDividerInsetStartResource(context, R.dimen.track_item_divider_start)
    return divider
}

internal fun RecyclerView.getPlaylistItemVerticalDivider(context: Context): MaterialDividerItemDecoration {
    val divider = MaterialDividerItemDecoration(context, LinearLayoutManager.VERTICAL)
    divider.setDividerInsetStartResource(context, R.dimen.playlist_item_divider_start)
    return divider
}

internal fun RecyclerView.getRadioStationItemVerticalDivider(context: Context): MaterialDividerItemDecoration {
    val divider = MaterialDividerItemDecoration(context, LinearLayoutManager.VERTICAL)
    divider.setDividerInsetStartResource(context, R.dimen.radio_item_divider_start)
    return divider
}

internal fun RecyclerView.getArtistItemVerticalDivider(context: Context): MaterialDividerItemDecoration {
    val divider = MaterialDividerItemDecoration(context, LinearLayoutManager.VERTICAL)
    divider.setDividerInsetStartResource(context, R.dimen.artist_item_divider_start)
    return divider
}