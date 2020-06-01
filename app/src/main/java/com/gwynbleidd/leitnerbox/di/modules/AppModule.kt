package com.gwynbleidd.leitnerbox.di.modules

import android.app.Application
import android.content.Context
import com.gwynbleidd.leitnerbox.database.entities.MyObjectBox
import dagger.Module
import dagger.Provides
import io.objectbox.BoxStore
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule(private val app: Application) {

    @Singleton
    @Provides
    @Named("Application")
    fun providesAppContext(): Context = app

    @Singleton
    @Provides
    fun provideBoxStore(@Named("Application") context: Context): BoxStore {
        return MyObjectBox.builder().androidContext(context).build()
    }
}