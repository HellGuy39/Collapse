package com.hellguy39.collapse.utils

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.MaterialToolbar

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

internal fun Fragment.setAnimations() {

}