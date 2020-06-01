package com.gwynbleidd.leitnerbox.di.modules

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.gwynbleidd.leitnerbox.di.scopes.ActivityScope
import com.gwynbleidd.leitnerbox.viewmodels.CardsViewModel
import com.gwynbleidd.leitnerbox.viewmodels.CardsViewModelFactory
import com.gwynbleidd.leitnerbox.views.activities.HomeActivity
import com.gwynbleidd.leitnerbox.views.adapters.CardGroupPageAdapter
import dagger.Module
import dagger.Provides

@Module
class CardsFragmentModule(private val activity: HomeActivity) {

    /*@ActivityScope
    @Provides
    fun provideAdapter(cardsViewModel : CardsViewModel) : CardGroupPageAdapter=  CardGroupPageAdapter(activity,cardsViewModel)*/

    @ActivityScope
    @Provides
    fun provideViewModel(viewModelFactory: CardsViewModelFactory) : CardsViewModel=
        ViewModelProviders.of(activity, viewModelFactory).get(CardsViewModel::class.java)
}