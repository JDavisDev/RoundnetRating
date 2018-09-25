package com.jdavis.roundnetrating.model

data class Game(var id: Int?, var round: Int, var teamOne: Team, var scoreOne: Int, var teamTwo: Team, var scoreTwo: Int) {
    var isReported: Boolean = false
}