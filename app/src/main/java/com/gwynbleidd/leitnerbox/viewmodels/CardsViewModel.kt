package com.gwynbleidd.leitnerbox.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gwynbleidd.leitnerbox.database.entities.CardEntity
import com.gwynbleidd.leitnerbox.database.entities.CardEntity_.groupId
import com.gwynbleidd.leitnerbox.repositories.CardsRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class CardsViewModel(private val repository: CardsRepository) : ViewModel() {

    private val disposable = CompositeDisposable()
    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    val doneTheWorkLiveData = MutableLiveData<Boolean>()

/*
    val cardsLiveData: ObjectBoxLiveData<CardEntity> = repository.getCards()
*/

    val cardBoxSize = repository.getCardBoxSize
    val groupBoxSize = repository.getGroupBoxSize

    fun getCardsOfDeck(groupId: Long,deck: Int) = repository.getCardsOfDeck(groupId,deck)

    fun getDeckSize(groupId: Long,deck: Int): Int = repository.deckSize(groupId,deck).toInt()

    fun addCard(groupId: Long,entities: List<CardEntity>) {
        disposable.add(
            repository.addToBox(groupId,entities).subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .subscribe({
                    Timber.tag("CardsViewModel").d("addCard COMPLETED : ${entities.size}")
                }, {
                    Timber.tag("CardsViewModel").d("addCard NOT COMPLETED : $it")
                })

        )
    }

    fun addNewCards(groupId: Long) {
        disposable.add(
            repository.addNewCards(groupId).subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .subscribe({
                    Timber.tag("CardsViewModel").d("addCard COMPLETED ")
                }, {
                    Timber.tag("CardsViewModel").d("addCard NOT COMPLETED : $it")
                })

        )
    }

    fun editCard(groupId: Long,editMode: String, state: Boolean, entity: CardEntity) {
        when (editMode) {
            "showAnswer" -> {
                entity.isFront = state
            }
            "makeFav" -> {
                entity.isFav = state
            }
            "makeDone" -> {
                entity.isDone = state
            }
        }
        disposable.add(
            repository.editEntity(groupId,entity)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Timber.tag("CardsViewModel").d("editCard COMPLETED : $entity")
                }, {
                    Timber.tag("CardsViewModel").d("editCard NOT COMPLETED : $it")
                })
        )
    }

    fun donDaysWord(groupId: Long,deck: Int) {
        disposable.add(
            repository.doneDaysWork(groupId,deck)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Timber.tag("CardsViewModel").d("addCard COMPLETED")
                    doneTheWorkLiveData.value = true
                }, {
                    Timber.tag("CardsViewModel").d("addCard NOT COMPLETED : $it")
                    doneTheWorkLiveData.value = false
                })
        )
    }


}