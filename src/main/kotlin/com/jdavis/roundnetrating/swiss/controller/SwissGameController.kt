package com.jdavis.roundnetrating.swiss.controller

import com.jdavis.roundnetrating.DatabaseDAO
import com.jdavis.roundnetrating.elo.controller.EloController
import com.jdavis.roundnetrating.model.Game
import com.jdavis.roundnetrating.model.Team
import com.jdavis.roundnetrating.swiss.model.SwissGameData
import tornadofx.*

class SwissGameController : Controller() {

    private val db: DatabaseDAO by inject()
    private val swissGameData: SwissGameData by inject()
    private val eloController: EloController by inject()

    fun submitGame(game: Game) {
        // save to our db
        updateStatsFromGame(game)
        db.updateGame(game)
        db.getTeams()
        swissGameData.gamesList = swissGameData.getAllGames()
    }

    private fun updateStatsFromGame(game: Game) {
        game.isReported = true
        val teamOne = getTeamByName(game.teamOne)
        val teamTwo = getTeamByName(game.teamTwo)

        // point diff
        teamOne.pointDiff += game.scoreOne - game.scoreTwo
        teamTwo.pointDiff += game.scoreTwo - game.scoreOne

        // Win/Loss and Points
        if (game.scoreOne > game.scoreTwo) {
            teamOne.wins++
            teamTwo.losses++
            teamOne.swissPoints++
        } else {
            teamOne.losses++
            teamTwo.wins++
            teamTwo.swissPoints++
        }

        db.updateTeam(game.teamOne, teamOne)
        db.updateTeam(game.teamTwo, teamTwo)
        eloController.updateEloOfMatch(game)
        swissGameData.teamsList = swissGameData.getAllTeams()
    }

    fun getTeamByName(name: String): Team {
        for (team in db.getTeams()) {
            if (team.name.equals(name, true)) {
                return team
            }
        }

        return Team()
    }
}