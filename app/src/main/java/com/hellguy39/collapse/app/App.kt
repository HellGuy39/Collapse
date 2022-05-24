package com.hellguy39.collapse.app

import android.app.Application
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.hellguy39.collapse.R
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        setupCustomErrorScreen()
    }

    private fun setupCustomErrorScreen() {
        CaocConfig.Builder.create()
            .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //default: CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM
            .enabled(true) //default: true
            .showErrorDetails(true) //default: true
            .showRestartButton(true) //default: true
            .logErrorOnRestart(true) //default: true
            .trackActivities(true) //default: false
            .minTimeBetweenCrashesMs(3000) //default: 3000
            .errorDrawable(R.drawable.ic_round_error_outline_64) //default: bug image
            .apply()
    }
}