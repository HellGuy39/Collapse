package com.hellguy39.collapse.utils

interface DialogEventListener {
    fun onPositiveButtonClick() = Unit
    fun onNegativeButtonClick() = Unit
    fun onNeutralButtonClick() = Unit
}