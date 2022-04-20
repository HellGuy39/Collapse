package com.hellguy39.domain.usecases

import android.content.res.Resources
import android.util.TypedValue

class GetColorFromThemeUseCase {
    operator fun invoke(theme: Resources.Theme, resId: Int): Int {
        val typedValue = TypedValue()
        theme.resolveAttribute(resId, typedValue, true)
        return typedValue.data
    }
}