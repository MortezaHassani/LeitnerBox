package com.gwynbleidd.leitnerbox.views.app

import android.app.Application
import com.gwynbleidd.leitnerbox.BuildConfig
import timber.log.Timber

class LeitnerApp : Application() {

    override fun onCreate() {
        super.onCreate()
        //add timber for debug mode
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

    }
}