package com.hellguy39.collapse.utils

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.divider.MaterialDividerItemDecoration
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

internal fun Fragment.setAnimations() {

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