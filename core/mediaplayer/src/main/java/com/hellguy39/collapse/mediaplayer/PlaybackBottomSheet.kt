package com.hellguy39.collapse.mediaplayer

import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback

class PlaybackBottomSheet(
    bottomSheetView: View,
): BottomSheetCallback() {

    private val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetView)

    init {
        configure()
    }

    private fun configure() {
//        bottomSheetBehavior.peekHeight = 48
//        bottomSheetBehavior.isHideable = false
        //bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        //bottomSheetBehavior.addBottomSheetCallback(this)
    }

    override fun onStateChanged(bottomSheet: View, newState: Int) {

    }

    override fun onSlide(bottomSheet: View, slideOffset: Float) {

    }

}