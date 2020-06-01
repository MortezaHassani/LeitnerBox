package com.gwynbleidd.leitnerbox.repositories

import android.provider.CalendarContract
import com.gwynbleidd.leitnerbox.R
import com.gwynbleidd.leitnerbox.database.entities.*
import com.gwynbleidd.leitnerbox.database.entities.CardGroupEntity_.name
import com.gwynbleidd.leitnerbox.di.scopes.ActivityScope
import io.objectbox.BoxStore
import io.objectbox.android.ObjectBoxLiveData
import io.objectbox.kotlin.equal
import io.reactivex.Completable
import io.reactivex.Single
import timber.log.Timber
import java.sql.Date
import java.sql.Timestamp
import java.time.Instant
import java.util.*
import javax.inject.Inject

@ActivityScope
class CardsRepository @Inject constructor(boxStore: BoxStore) {

    private val cardsBox = boxStore.boxFor(CardEntity::class.java)
    private val groupBox = boxStore.boxFor(CardGroupEntity::class.java)

    fun getCards(groupId: Long): ObjectBoxLiveData<CardEntity> =
        ObjectBoxLiveData(cardsBox.query().equal(CardEntity_.groupId, groupId).build())

    val getCardBoxSize = cardsBox.all.size

    val getGroupBoxSize = groupBox.all.size

    fun getCardsOfDeck(groupId: Long, deck: Int): ObjectBoxLiveData<CardEntity> =
        ObjectBoxLiveData(
            cardsBox.query().equal(CardEntity_.deck, deck).equal(CardEntity_.groupId, groupId)
                .build()
        )

    fun deckSize(groupId: Long, deck: Int) =
        cardsBox.query().equal(CardEntity_.deck, deck).equal(CardEntity_.groupId, groupId).build()
            .count()

    fun doneDaysWork(groupId: Long, deck: Int): Completable {
        val cardDeck = cardsBox.query().equal(CardEntity_.deck, deck).build().find()
        var emptyDeck = 1
        if (deckSize(groupId, 0) < getCardBoxSize)
            emptyDeck = findFirstEmptyDeck(groupId, deck)
        return try {
            cardDeck.forEach {
                if (it.isDone != null) {
                    if (it.isDone!!) {
                        it.deck = emptyDeck
                    } else {
                        it.deck = 0
                    }
                } else {
                    it.deck = 0
                }
                it.isDone = null
                it.isFront = true
                cardsBox.put(it)
            }
            addNewCards(groupId)
            addRevCards(groupId)
            leitnerSort(groupId)
            Completable.complete()
        } catch (e: Exception) {
            Completable.error(Throwable(e))
        }

    }

    fun addToBox(groupId: Long, entities: List<CardEntity>): Completable {
        return try {
            entities.forEach { entity ->
                if (cardsBox.query().equal(
                        CardEntity_.id,
                        entity.id
                    ).equal(CardEntity_.groupId, groupId)
                        .build().find().isEmpty()
                )
                    cardsBox.put(entity)
            }
            Completable.complete()
        } catch (e: Exception) {
            Completable.error(Throwable(e))
        }
    }

    fun editEntity(groupId: Long, entity: CardEntity): Completable {
        return try {
            val editEntity = cardsBox.query().equal(CardEntity_.id, entity.id).equal(CardEntity_.groupId, groupId).build().findUnique()
            entity.entityId = editEntity!!.entityId
            cardsBox.put(entity)
            Completable.complete()
        } catch (e: Exception) {
            Completable.error(Throwable(e))
        }
    }

    private fun findFirstEmptyDeck(groupId: Long, deck: Int): Int {
        var emptyDeck = deck
        return if (deck < 30) {
            for (i in deck + 1..30) {
                if (cardsBox.query().equal(CardEntity_.deck, i).equal(CardEntity_.groupId, groupId).build().find().isEmpty()) {
                    emptyDeck = i
                    break
                }
            }
            emptyDeck
        } else {
            -1
        }
    }

    private fun findLastEmptyDeck(groupId: Long ): Int {
        var emptyDeck = -1
        for (i in 30 downTo 1) {
            if (
                cardsBox.query().equal(CardEntity_.deck, i).equal(CardEntity_.groupId, groupId).build().find().isEmpty()
                && cardsBox.query().equal(CardEntity_.deck, i - 1).equal(CardEntity_.groupId, groupId).build().find().isNotEmpty()
            ) {
                emptyDeck = i
                break
            }
        }
        return emptyDeck
    }

    private fun leitnerSort(groupId: Long ): Completable {
        return try {
            for (i in 30 downTo 1) {
                if (deckSize(groupId,i) != 0L) {
                    val deckEntity: MutableList<CardEntity> =
                        cardsBox.query().equal(CardEntity_.deck, i).build().find()
                    for (card in deckEntity) {
                        if (i >= 30)
                            card.deck = -1
                        card.deck++
                        cardsBox.put(card)
                    }
                }
            }
            Completable.complete()
        } catch (e: Exception) {
            Completable.error(Throwable(e))
        }
    }

    fun addNewCards(groupId: Long ): Completable {
        return try {
            val cardList: MutableList<CardEntity> = mutableListOf()
            for (i in 0..10) {
                val card = CardEntity(
                    id = (Date().time / 1000).toInt() + i,
                    front = "Front #$i \n ${Timestamp(System.currentTimeMillis())}",
                    back = "Back #$i",
                    deck = 0
                )
                cardList.add(card)
            }
            addToBox(groupId,cardList)
            Completable.complete()
        } catch (e: Exception) {
            Completable.error(Throwable(e))
        }

    }

    private fun addRevCards(groupId: Long ): Completable {
        return try {
            val emptyDeck = findLastEmptyDeck(groupId)
            if (emptyDeck == 3 || emptyDeck == 7 || emptyDeck == 15 || emptyDeck == 30) {
                val cardDeck =
                    cardsBox.query().equal(CardEntity_.deck, emptyDeck - 1).equal(CardEntity_.groupId, groupId).build().find()
                cardDeck.forEach {
                    it.deck = 0
                    it.isDone = null
                    it.isFront = true
                    cardsBox.put(it)
                }
            }
            Completable.complete()
        } catch (e: Exception) {
            Completable.error(Throwable(e))
        }
    }

    /* private fun lastGroupCardDeck(): Int {
         val cardGroupSize = groupBox.all.size
         val lastCardGroupDeck = groupBox.all[cardGroupSize - 1]
         return lastCardGroupDeck.deck
     }*/

    fun addGroupCard(groupName: String, groupColor: Int): Completable {
        return try {
            val cardGroupEntity =
                CardGroupEntity(name = groupName, color = groupColor, deck = 0)
            Completable.complete()
        } catch (e: Exception) {
            Completable.error(Throwable(e))
        }
    }

    fun removeGroupCard(groupId: Long): Completable {
        return try {
            groupBox.remove(groupId)
            val cards = cardsBox.query().equal(CardEntity_.groupId, groupId).build().find()
            cards.forEach {
                groupBox.remove(it.entityId)
            }
            Completable.complete()
        } catch (e: Exception) {
            Completable.error(Throwable(e))
        }
    }


/*
    fun getDeck(deck: Int) =
        Single.just(cardsBox.query().equal(CardEntity_.deck, deck).build().find())
*/
/*

    private fun boxRev(x: Int, day: Int): Completable {
        return try {
            */
/*
            pick the card in box = x and part = 0
            if (answer){
                put the card in box = x+1 and part = day-(x+1)
            } else {
                put the card in box =0 and part = 0
            }
            *//*

            Completable.complete()
        } catch (e: Exception) {
            Completable.error(Throwable(e))
        }
    }

    private fun boxSort(x: Int): Completable {
        return try {
            */
/*
            this changes should be apply to the number of part and box of cart :
            for  all parts in the box = x
            if(part>0) -> part -- ;
            *//*

            Completable.complete()
        } catch (e: Exception) {
            Completable.error(Throwable(e))
        }
    }

    private fun boxNew(): Completable {
        return try {
            */
/*
             pick the card in box = 0 and part = 0
            if (answer){
                put the card in box = 1 and part = 0
            } else {
                put the card in box =0 and part = 0
            }
            *//*

            Completable.complete()
        } catch (e: Exception) {
            Completable.error(Throwable(e))
        }
    }

    fun boxPrepareCards(day: Int): Completable {
        return try {
            when {
                day in 2..5 -> {
                    boxRev(1, day)
                    boxSort(1)
                }
                day in 6..13 -> {
                    for (i in 2 downTo 1) {
                        boxRev(i, day)
                        boxSort(i)
                    }
                }
                day in 14..29 -> {
                    for (i in 3 downTo 1) {
                        boxRev(i, day)
                        boxSort(i)
                    }
                }
                day >= 30 -> {
                    for (i in 4 downTo 1) {
                        boxRev(i, day)
                        boxSort(i)
                    }
                }
            }
            boxNew()
            Completable.complete()
        } catch (e: Exception) {
            Completable.error(Throwable(e))
        }
    }

*/
/*

    private fun leitnerFunction(card: CardEntity, ans: Boolean, emptyDeck: Int) {

        val cardFromDeck = cardsBox.query().equal(CardEntity_.id, card.id).build().findUnique()
        cardFromDeck?.let {
            if (ans) {
                it.deck = emptyDeck
            } else {
                it.deck = 0
            }
            cardsBox.put(it)
        }

    }
*/
/*
    fun leitnerDeckToPick(): Int {
        val emptyDeck = findFirstEmptyDeck(0)
        return when {
            emptyDeck in 1..2 -> {
                0
            }
            emptyDeck in 3..6 -> {
                2
            }
            emptyDeck in 7..14 -> {
                6
            }
            emptyDeck >= 15 -> {
                14
            }
            else -> {
                0
            }
        }
    }
*/


}