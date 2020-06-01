package com.gwynbleidd.leitnerbox.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gwynbleidd.leitnerbox.di.scopes.ActivityScope
import com.gwynbleidd.leitnerbox.repositories.CardsRepository
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
@ActivityScope
class CardsViewModelFactory @Inject constructor(
    private val repository: CardsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CardsViewModel(
            repository
        ) as T
    }

}