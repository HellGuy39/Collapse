package com.hellguy39.domain.usecases

import java.text.SimpleDateFormat
import java.util.*

private const val PATTERN = "m:ss"

class DateFormatUseCase {
    operator fun invoke(date: Long): String {
        return SimpleDateFormat(PATTERN, Locale.getDefault()).format(Date(date))
    }
}