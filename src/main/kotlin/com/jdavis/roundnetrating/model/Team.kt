package com.jdavis.roundnetrating.model

data class Team(val id: Int) {
    var name = "Team" + id.toString()
    var eloRating = 1500
    lateinit var playerOne: Player
    lateinit var playerTwo: Player
    var wins = 0
    var losses = 0
    var swissPoints = 0
    var pointDiff = 0
}