package com.gwynbleidd.leitnerbox.di.components

import com.gwynbleidd.leitnerbox.di.modules.CardsFragmentModule
import com.gwynbleidd.leitnerbox.di.scopes.ActivityScope
import com.gwynbleidd.leitnerbox.views.fragments.CardsPageFragment
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [CardsFragmentModule::class])
interface CardsFragmentComponent {
    fun inject(cardsPageFragment: CardsPageFragment)

    @Subcomponent.Builder
    interface Builder {
        fun build(): CardsFragmentComponent
        fun setContext(cardsFragmentModule: CardsFragmentModule): Builder
    }
}