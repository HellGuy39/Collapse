package com.hellguy39.collapse.app

import android.app.Application
import com.hellguy39.collapse.di.AppComponent
import com.hellguy39.collapse.di.AppModule
import com.hellguy39.collapse.di.DaggerAppComponent

class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.create()
            /*.builder()
            .appModule(AppModule(context = context))
            .build()*/

    }
}