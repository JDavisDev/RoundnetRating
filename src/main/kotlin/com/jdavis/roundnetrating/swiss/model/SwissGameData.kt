package com.jdavis.roundnetrating.swiss.model

import com.jdavis.roundnetrating.model.Game

class SwissGameData {
    var gameList: HashMap<Int, MutableList<Game>> = hashMapOf()

    fun insertGame(round: Int, game: Game) {
        if (gameList.containsKey(round)) {
            gameList[round]?.add(game)
        } else {
            gameList[round] = mutableListOf()
            gameList[round]?.add(game)
        }
    }

    fun getGamesInRound(round: Int): MutableList<Game> {
        return if (gameList.containsKey(round)) {
            gameList.getValue(round)
        } else {
            gameList[round] = mutableListOf()
            mutableListOf()
        }
    }

    fun getAllGames(): Collection<MutableList<Game>> {
        return gameList.values
    }
}
