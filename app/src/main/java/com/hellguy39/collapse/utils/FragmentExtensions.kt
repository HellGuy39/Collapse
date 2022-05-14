package com.hellguy39.collapse.utils

import android.Manifest
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialFadeThrough
import com.hellguy39.collapse.R

internal fun Fragment.setSharedElementTransitionAnimation() {
    sharedElementEnterTransition = MaterialContainerTransform().apply {
        drawingViewId = R.id.fragmentContainer
        //scrimColor = Color.TRANSPARENT
        setAllContainerColors(
            requireActivity().theme.getColorByResId(
                com.google.android.material.R.attr.colorSurface
            )
        )
    }
}

internal fun Fragment.setMaterialFadeThoughtAnimation() {
    enterTransition = MaterialFadeThrough()
    reenterTransition = MaterialFadeThrough()
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
