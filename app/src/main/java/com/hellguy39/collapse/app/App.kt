package com.hellguy39.collapse.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.hellguy39.collapse.R
import com.hellguy39.collapse.mediaplayer.MediaPlayerNotificationManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        MediaPlayerNotificationManager.createMediaPlayerNotificationChannel(this)
    }

}