package main.model

import tornadofx.*

class Player(id: Int, name: String, eloRating: Int) {
    var id by property(id)
    fun idProperty() = getProperty(Player::id)

    var name by property(name)
    fun nameProperty() = getProperty(Player::name)

    var eloRating by property(eloRating)
    fun eloRatingProperty() = getProperty(Player::eloRating)
}