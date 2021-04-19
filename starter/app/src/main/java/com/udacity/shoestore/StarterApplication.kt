package com.udacity.shoestore

import android.app.Application
import timber.log.Timber

class StarterApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}