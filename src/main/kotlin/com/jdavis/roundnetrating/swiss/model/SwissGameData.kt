package com.jdavis.roundnetrating.swiss.model

import com.jdavis.roundnetrating.DatabaseDAO
import com.jdavis.roundnetrating.model.Game
import com.jdavis.roundnetrating.model.Team
import tornadofx.*

class SwissGameData : Controller() {
    private val db: DatabaseDAO by inject()

    var gamesMap: HashMap<Int, MutableList<Game>> = hashMapOf()
    var gamesList: MutableList<Game> = mutableListOf()
    var teamsList: MutableList<Team> = mutableListOf()

    fun insertGame(round: Int, game: Game) {
        if (!gamesMap.containsKey(round)) {
            gamesMap[round] = mutableListOf()
        }

        gamesList.add(game)
        gamesMap[round]?.add(game)
    }

    fun insertGameIntoDb(game: Game) {
        db.insertGame(game)
    }

    fun getGamesInRound(round: Int): MutableList<Game> {
        gamesMap.clear()

        for (game in db.getAllGames()) {
            insertGame(game.round, game)
        }

        return if (gamesMap.containsKey(round)) {
            gamesMap.getValue(round)
        } else {
            gamesMap[round] = mutableListOf()
            mutableListOf()
        }
    }

    fun getAllGames(): MutableList<Game> {
        gamesList.clear()

        for (game in db.getAllGames()) {
            gamesList.add(game)
        }

        return gamesList
    }

    /**
     * Team Section
     */
    fun getAllTeams(): MutableList<Team> {
        teamsList.clear()

        for (team in db.getTeams()) {
            teamsList.add(team)
        }

        return teamsList
    }

    fun insertTeam(team: Team) {
        teamsList.add(team)
    }
}
