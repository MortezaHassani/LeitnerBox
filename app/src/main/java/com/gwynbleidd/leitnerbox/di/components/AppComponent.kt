package com.gwynbleidd.leitnerbox.di.components

import com.gwynbleidd.leitnerbox.di.modules.AppModule
import com.gwynbleidd.leitnerbox.di.modules.CardSModule
import com.gwynbleidd.leitnerbox.views.app.LeitnerApp
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(app: LeitnerApp)
    fun injectCardsActivity(): CardsActivityComponent.Builder
    fun injectCardsFragment() : CardsFragmentComponent.Builder

    @Component.Builder
    interface Builder {
        fun build(): AppComponent
        fun setContext(appModule: AppModule): Builder
    }
}