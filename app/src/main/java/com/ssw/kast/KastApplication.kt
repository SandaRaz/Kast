package com.ssw.kast

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class KastApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d("KastApplication","From now, Kast application is running")
    }
}