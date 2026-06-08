package com.trackpets.app

import android.app.Application
import com.trackpets.app.data.repository.AppContainer

class TrackPetsApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
