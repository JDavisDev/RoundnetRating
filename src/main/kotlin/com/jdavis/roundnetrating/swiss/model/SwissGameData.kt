package com.jdavis.roundnetrating.swiss.model

import com.jdavis.roundnetrating.DatabaseDAO
import com.jdavis.roundnetrating.model.Game
import tornadofx.*

class SwissGameData : Controller() {
    private val db: DatabaseDAO by inject()

    var gameList: HashMap<Int, MutableList<Game>> = hashMapOf()

    fun insertGame(round: Int, game: Game) {
        if (!gameList.containsKey(round)) {
            gameList[round] = mutableListOf()
        }

        gameList[round]?.add(game)
    }

    fun insertGameIntoDb(game: Game) {
        db.insertGame(game)
    }

    fun getGamesInRound(round: Int): MutableList<Game> {
        val allGamesList = db.getAllGames()
        gameList.clear()

        for (game in allGamesList) {
            insertGame(game.round, game)
        }

        return if (gameList.containsKey(round)) {
            gameList.getValue(round)
        } else {
            gameList[round] = mutableListOf()
            mutableListOf()
        }
    }
}
