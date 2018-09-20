package com.jdavis.roundnetrating.model

import tornadofx.*

class Player(id: Int, name: String, eloRating: Int) {
    var id: Int by property(id)
    fun idProperty() = getProperty(Player::id)

    var name: String by property(name)
    fun nameProperty() = getProperty(Player::name)

    var eloRating: Int by property(eloRating)
    fun eloRatingProperty() = getProperty(Player::eloRating)
}

class PlayerModel(player: Player? = null) : ItemViewModel<Player>(player) {
    val id = bind(Player::idProperty)
    val name = bind(Player::nameProperty)
    val eloRating = bind(Player::eloRatingProperty)
}