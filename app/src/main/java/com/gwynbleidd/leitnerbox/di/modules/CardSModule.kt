package com.gwynbleidd.leitnerbox.di.modules

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import com.gwynbleidd.leitnerbox.di.scopes.ActivityScope
import com.gwynbleidd.leitnerbox.viewmodels.CardsViewModel
import com.gwynbleidd.leitnerbox.viewmodels.CardsViewModelFactory
import com.gwynbleidd.leitnerbox.views.activities.CardsActivity
import com.gwynbleidd.leitnerbox.views.adapters.CardStudyPageAdapter
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class CardSModule(private val activity: CardsActivity) {


    @ActivityScope
    @Provides
    @Named("CardActivity")
    fun provideActivityContext(): Context = activity


    @ActivityScope
    @Provides
    fun provideViewPagerAdapter() =
        CardStudyPageAdapter(activity)


    @ActivityScope
    @Provides
    fun provideViewModel(viewModelFactory: CardsViewModelFactory) =
        ViewModelProviders.of(activity, viewModelFactory).get(CardsViewModel::class.java)


}