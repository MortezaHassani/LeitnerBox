package com.gwynbleidd.leitnerbox.di.components

import com.gwynbleidd.leitnerbox.di.modules.CardSModule
import com.gwynbleidd.leitnerbox.di.scopes.ActivityScope
import com.gwynbleidd.leitnerbox.views.activities.CardsActivity
import com.gwynbleidd.leitnerbox.views.fragments.CardsPageFragment
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [CardSModule::class])
interface CardsActivityComponent {
    fun inject(cardsActivity: CardsActivity)
    fun inject(cardsPageFragment: CardsPageFragment)

    @Subcomponent.Builder
    interface Builder {
        fun build(): CardsActivityComponent
        fun setContext(cardSModule: CardSModule): Builder
    }
}