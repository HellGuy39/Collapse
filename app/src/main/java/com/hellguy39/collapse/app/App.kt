package com.hellguy39.collapse.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    /*lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.create()
            *//*.builder()
            .appModule(AppModule(context = context))
            .build()*//*

    }*/
}