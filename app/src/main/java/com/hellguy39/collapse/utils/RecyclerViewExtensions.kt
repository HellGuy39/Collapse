package com.hellguy39.collapse.utils

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.hellguy39.collapse.R

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

internal fun RecyclerView.getVerticalLayoutManager(context: Context?): LinearLayoutManager {
    return LinearLayoutManager(
        context,
        LinearLayoutManager.VERTICAL,
        false
    )
}

internal fun RecyclerView.getGridLayoutManager(context: Context?): GridLayoutManager {
    return GridLayoutManager(
        context,
        2,
        GridLayoutManager.VERTICAL,
        false
    )
}