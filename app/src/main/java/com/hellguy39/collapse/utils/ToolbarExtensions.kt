package com.hellguy39.collapse.utils

import android.app.Activity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.material.appbar.MaterialToolbar

internal fun MaterialToolbar.setOnBackFragmentNavigation() {
    this.setNavigationOnClickListener {
        it.findNavController().popBackStack()
    }
}

internal fun MaterialToolbar.setOnBackActivityNavigation(activity: Activity) {
    this.setNavigationOnClickListener {
        activity.onBackPressed()
    }
}
