package main.model

data class Team(val id: Int) {
    var eloRating = 1500
    lateinit var playerOne: Player
    lateinit var playerTwo: Player
}