package com.hellguy39.collapse.core.ui.extensions

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.hellguy39.collapse.core.ui.R

private const val DEFAULT_SPAN_COUNT = 2

fun RecyclerView.setVerticalLayoutManager() {
    layoutManager = LinearLayoutManager(
        context,
        LinearLayoutManager.VERTICAL,
        false
    )
}

fun RecyclerView.setHorizontalLayoutManager() {
    layoutManager = LinearLayoutManager(
        context,
        LinearLayoutManager.HORIZONTAL,
        false
    )
}

fun RecyclerView.setGridLayoutManager(spanCount: Int = DEFAULT_SPAN_COUNT) {
    layoutManager = GridLayoutManager(
        context,
        spanCount,
        GridLayoutManager.VERTICAL,
        false
    )
}

fun RecyclerView.setHorizontalDivider() {
    val dividerItemDecoration = MaterialDividerItemDecoration(
        context,
        LinearLayoutManager.HORIZONTAL
    )
    dividerItemDecoration.setDividerInsetStartResource(context, R.dimen.default_divider_inset)
    dividerItemDecoration.setDividerInsetEndResource(context, R.dimen.default_divider_inset)
    dividerItemDecoration.setDividerColorResource(context, R.color.divider_item_color)
    addItemDecoration(dividerItemDecoration)
}

fun RecyclerView.setVerticalDivider() {
    val dividerItemDecoration = MaterialDividerItemDecoration(
        context,
        LinearLayoutManager.VERTICAL
    )
    dividerItemDecoration.setDividerInsetStartResource(context, R.dimen.default_divider_inset)
    dividerItemDecoration.setDividerInsetEndResource(context, R.dimen.default_divider_inset)
    dividerItemDecoration.setDividerColorResource(context, R.color.divider_item_color)
    addItemDecoration(dividerItemDecoration)
}

fun <T> RecyclerView.clearAndUpdateDataSet(adapterDataSet: MutableList<T>, newData: List<T>) {
    // Clear old data
    val previousSize = adapter?.itemCount ?: 0
    adapterDataSet.clear()
    // Set new data
    adapter?.notifyItemRangeRemoved(0, previousSize)
    adapterDataSet.addAll(newData)
    adapter?.notifyItemRangeInserted(0, adapterDataSet.size)
}