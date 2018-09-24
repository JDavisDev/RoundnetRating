package com.jdavis.roundnetrating.swiss.model

import com.jdavis.roundnetrating.model.Game

class SwissGameData {
    var gameList: HashMap<Int, MutableList<Game>> = hashMapOf()

    public fun insertGame(round: Int, game: Game) {
        if (gameList.containsKey(round)) {
            gameList[round]?.add(game)
        } else {
            gameList[round] = mutableListOf()
            gameList[round]?.add(game)
        }
    }

    public fun getGamesInRound(round: Int): MutableList<Game> {
        if (gameList.containsKey(round)) {
            return gameList.getValue(round)
        } else {
            gameList[round] = mutableListOf()
            return mutableListOf()
        }
    }

    public fun getAllGames(): Collection<MutableList<Game>> {
        return gameList.values
    }
}
