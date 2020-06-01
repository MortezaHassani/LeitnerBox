package com.gwynbleidd.leitnerbox.views.app

import android.app.Application
import com.gwynbleidd.leitnerbox.BuildConfig
import com.gwynbleidd.leitnerbox.di.components.AppComponent
import com.gwynbleidd.leitnerbox.di.components.DaggerAppComponent
import com.gwynbleidd.leitnerbox.di.modules.AppModule
import io.objectbox.BoxStore
import timber.log.Timber
import javax.inject.Inject

class LeitnerApp : Application() {

    private lateinit var appComponent: AppComponent


    override fun onCreate() {
        super.onCreate()
        //add timber for debug mode
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        appComponent = DaggerAppComponent
            .builder()
            .setContext(AppModule(this))
            .build()

    }

    fun provideAppComponent(): AppComponent {
        if (!this::appComponent.isInitialized) {
            appComponent = DaggerAppComponent
                .builder()
                .setContext(AppModule(this))
                .build()
        }
        return appComponent
    }
}