package com.gwynbleidd.leitnerbox.database.entities

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class CardEntity(
    @Id var entityId: Long = 0,
    var id: Int = 0,
    var front: String = "Front #0",
    var back: String = "Back #0",
    var isFront: Boolean = true,
    var isFav: Boolean = false,
    var isDone: Boolean? = null,
    var groupId : Long = 0L,
    var deck: Int = 0 // favCardDeck = 00
)