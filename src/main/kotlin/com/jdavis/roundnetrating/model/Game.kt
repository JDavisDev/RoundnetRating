package com.jdavis.roundnetrating.model

import tornadofx.*

class Game(id: Int?, round: Int, teamOne: String, scoreOne: Int, teamTwo: String, scoreTwo: Int, isReported: Boolean) {
    var id: Int by property(id)
    fun idProperty() = getProperty(Game::id)

    var round: Int by property(round)
    fun roundProperty() = getProperty(Game::round)

    var teamOne: String by property(teamOne)
    fun teamOneProperty() = getProperty(Game::teamOne)

    var scoreOne: Int by property(scoreOne)
    fun scoreOneProperty() = getProperty(Game::scoreOne)

    var teamTwo: String by property(teamTwo)
    fun teamTwoProperty() = getProperty(Game::teamTwo)

    var scoreTwo: Int by property(scoreTwo)
    fun scoreTwoProperty() = getProperty(Game::scoreTwo)

    var isReported: Boolean by property(isReported)
    fun isReportedProperty() = getProperty(Game::isReported)
}