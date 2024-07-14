package com.posttrip.journeydex

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class JourneydexApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}