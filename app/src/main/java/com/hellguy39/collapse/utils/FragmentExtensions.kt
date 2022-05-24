package com.hellguy39.collapse.utils

import android.content.Context
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.transition.platform.MaterialArcMotion
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialFadeThrough
import com.hellguy39.collapse.R
import com.hellguy39.collapse.di.PREFS_NAME

private const val IS_ANIMATIONS_ENABLED = "is_animations_enabled"
private const val IS_SAVE_STATE_ENABLED = "is_save_state_enabled"

private fun isAnimationsEnabled(context: Context): Boolean {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    return prefs.getBoolean(IS_ANIMATIONS_ENABLED, true)
}

internal fun Fragment.setSharedElementTransitionAnimation() {
    if (!isAnimationsEnabled(requireContext()))
        return

    sharedElementEnterTransition = MaterialContainerTransform().apply {
        drawingViewId = R.id.fragmentContainer
        //scrimColor = Color.TRANSPARENT
        setAllContainerColors(
            requireActivity().theme.getColorByResId(
                com.google.android.material.R.attr.colorSurface
            )
        )
        duration = 300L
        pathMotion = MaterialArcMotion()
    }
}

internal fun Fragment.setMaterialFadeThoughtAnimation() {
    if (!isAnimationsEnabled(requireContext()))
        return

    enterTransition = MaterialFadeThrough().apply {
        duration = 300L
    }
    reenterTransition = MaterialFadeThrough().apply {
        duration = 250L
    }
}

internal fun Fragment.showDialog(
    title: String = "",
    message: String = "",
    iconId: Int,
    negativeButtonText: String = "Cancel",
    positiveButtonText: String = "Ok",
    dialogEventListener: DialogEventListener
) {
    MaterialAlertDialogBuilder(requireContext())
        .setTitle(title)
        .setMessage(message)
        .setNegativeButton(negativeButtonText) { dialog, which ->
            dialog.dismiss()
            dialogEventListener.onNegativeButtonClick()
        }
        .setPositiveButton(positiveButtonText) { dialog, which ->
            dialog.dismiss()
            dialogEventListener.onPositiveButtonClick()
        }
        .setIcon(iconId)
        .show()
}
