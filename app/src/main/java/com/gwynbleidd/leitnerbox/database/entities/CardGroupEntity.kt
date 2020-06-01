package com.gwynbleidd.leitnerbox.database.entities

import com.gwynbleidd.leitnerbox.R
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import java.util.*

@Entity
data class CardGroupEntity(
    @Id var Id : Long = (Date().time / 1000),
    var name: String = "Card Deck #0",
    var color: Int = R.color.colorPrimary,
    var deck: Int = 0
)