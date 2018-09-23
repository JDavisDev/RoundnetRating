package com.jdavis.roundnetrating.model

import tornadofx.*

class Team(id: Int = 0, name: String = id.toString(), eloRating: Int = 1500,
           playerOne: Player = Player(1, "one", 1500),
           playerTwo: Player = Player(2, "two", 1500),
           wins: Int = 0, losses: Int = 0, swissPoints: Int = 0, pointDiff: Int = 0,
           hadBye: Boolean = false) {

    var id: Int by property(id)
    fun idProperty() = getProperty(Team::id)

    var name: String by property(name)
    fun nameProperty() = getProperty(Team::name)

    var eloRating: Int by property(eloRating)
    fun eloRatingProperty() = getProperty(Team::eloRating)

    var playerOne: Player by property(playerOne)
    fun playerOneProperty() = getProperty(Team::playerOne)

    var playerTwo: Player by property(playerTwo)
    fun playerTwoProperty() = getProperty(Team::playerTwo)

    var wins: Int by property(wins)
    fun winsProperty() = getProperty(Team::wins)

    var losses: Int by property(losses)
    fun lossesProperty() = getProperty(Team::losses)

    var swissPoints: Int by property(swissPoints)
    fun swissPointsProperty() = getProperty(Team::swissPoints)

    var pointDiff: Int by property(pointDiff)
    fun pointDiffProperty() = getProperty(Team::pointDiff)

    var hadBye: Boolean by property(hadBye)
    fun hadByeProperty() = getProperty(Team::hadBye)
}

class TeamModel(team: Team? = null) : ItemViewModel<Team>(team) {
    val id = bind(Team::idProperty)
    val name = bind(Team::nameProperty)
    val eloRating = bind(Team::eloRatingProperty)
    val playerOne = bind(Team::playerOneProperty)
    val playerTwo = bind(Team::playerTwoProperty)
    val wins = bind(Team::winsProperty)
    val losses = bind(Team::lossesProperty)
    val swissPoints = bind(Team::swissPointsProperty)
    val pointDiff = bind(Team::pointDiffProperty)
    val hadBye = bind(Team::hadByeProperty)
}