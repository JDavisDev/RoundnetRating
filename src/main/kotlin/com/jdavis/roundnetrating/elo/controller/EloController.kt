package com.jdavis.roundnetrating.elo.controller

import com.jdavis.roundnetrating.model.Game
import com.jdavis.roundnetrating.model.Team
import tornadofx.*
import java.lang.Math.abs
import kotlin.math.roundToInt

class EloController : Controller() {

    private val ratingScaleK = 32
    private var ratingDelta = 0
    private var teamOneExpectedWinRate: Double = 0.5

    fun updateEloOfMatch(game: Game) {
        game.teamOne.eloRating = getTeamEloRating(game.teamOne)
        game.teamTwo.eloRating = getTeamEloRating(game.teamTwo)

        val winningTeam = getWinningTeam(game)
        val losingTeam = getLosingTeam(game)

        setTeamExpectedWinRate(game.teamOne.eloRating, game.teamTwo.eloRating)
        val marginOfVictoryMultiplier = getMOVOne(abs(game.scoreOne - game.scoreTwo), winningTeam.eloRating, losingTeam.eloRating)
        ratingDelta = getRatingDelta(marginOfVictoryMultiplier).roundToInt()

        updateTeamELO(winningTeam, losingTeam)
    }

    private fun getTeamEloRating(team: Team) : Int {
        return (team.playerOne.eloRating + team.playerTwo.eloRating) / 2
    }

    private fun setTeamExpectedWinRate(teamOneRating: Int, teamTwoRating: Int) {
        // Probability of teamOne winning = 0...1 / (10^(ELODIFF/400) + 1)
        val teamOneVal = Math.pow(10.0, (teamOneRating.toDouble()) / 400.0)
        val teamTwoVal = Math.pow(10.0, (teamTwoRating.toDouble()) / 400.0)
        teamOneExpectedWinRate = teamOneVal / (teamOneVal + teamTwoVal)
    }

    private fun getWinningTeam(game: Game): Team {
        return when {
            game.scoreOne > game.scoreTwo -> game.teamOne
            else -> game.teamTwo
        }
    }

    private fun getLosingTeam(game: Game): Team {
        return when {
            game.scoreOne < game.scoreTwo -> game.teamOne
            else -> game.teamTwo
        }
    }

    private fun getRatingDelta(marginOfVictoryMultiplier: Double): Double {
        return ratingScaleK * (1 - teamOneExpectedWinRate) * marginOfVictoryMultiplier
    }

    private fun getMOVOne(pointDifferential: Int, winningTeamElo: Int, losingTeamElo: Int): Double {
        //Margin of Victory Multiplier = LN(ABS(PD)+1) * (2.2/((ELOW-ELOL)*.001+2.2))
        return kotlin.math.ln(abs(pointDifferential) + 1.0) * (2.2 / ((winningTeamElo - losingTeamElo) * .001 + 2.2))
    }

    private fun updateTeamELO(winningTeam: Team, losingTeam: Team) {
        updatePlayerElo(winningTeam, ratingDelta)
        updatePlayerElo(losingTeam, -ratingDelta)
    }

    private fun updatePlayerElo(team: Team, ratingDelta: Int) {
        team.playerOne.eloRating += ratingDelta
        team.playerTwo.eloRating += ratingDelta
    }
}