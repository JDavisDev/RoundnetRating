package com.jdavis.roundnetrating.controller

import main.model.Match
import main.model.Team
import tornadofx.*
import java.lang.Math.abs
import kotlin.math.roundToInt

class EloController : Controller() {

    val RATING_FACTOR_K = 32
    var ratingDelta = 0
    var teamOneExpectedWinRate: Double = 0.5

    fun updateEloOfMatch(match: Match) {
        match.teamOne.eloRating = getTeamEloRating(match.teamOne)
        match.teamTwo.eloRating = getTeamEloRating(match.teamTwo)

        val winningTeam = getWinningTeam(match)
        val losingTeam = getLosingTeam(match)

        setTeamExpectedWinRate(match.teamOne.eloRating, match.teamTwo.eloRating)
        val marginOfVictoryMultiplier = getMOVOne(abs(match.scoreOne - match.scoreTwo), winningTeam.eloRating, losingTeam.eloRating)
        ratingDelta = getRatingDelta(marginOfVictoryMultiplier).roundToInt()

        updateTeamELO(winningTeam, losingTeam)
    }

    private fun getTeamEloRating(team: Team) : Int {
        return (team.playerOne.eloRating + team.playerTwo.eloRating) / 2
    }

    private fun getExpectedWinner(match: Match): Int {
        return when {
            match.teamOne.eloRating > match.teamTwo.eloRating -> match.teamOne.id
            match.teamOne.eloRating == match.teamTwo.eloRating -> -1
            else -> match.teamTwo.id
        }
    }

    private fun setTeamExpectedWinRate(teamOneRating: Int, teamTwoRating: Int) {
        // Probability of teamOne winning = 0...1 / (10^(ELODIFF/400) + 1)
        val teamOneVal = Math.pow(10.0, (teamOneRating.toDouble()) / 400.0)
        val teamTwoVal = Math.pow(10.0, (teamTwoRating.toDouble()) / 400.0)
        teamOneExpectedWinRate = teamOneVal / (teamOneVal + teamTwoVal)
    }

    private fun getWinningTeam(match: Match): Team {
        return when {
            match.scoreOne > match.scoreTwo -> match.teamOne
            else -> match.teamTwo
        }
    }

    private fun getLosingTeam(match: Match): Team {
        return when {
            match.scoreOne < match.scoreTwo -> match.teamOne
            else -> match.teamTwo
        }
    }

    private fun getRatingDelta(marginOfVictoryMultiplier: Double): Double {
        return RATING_FACTOR_K * (1 - teamOneExpectedWinRate) * marginOfVictoryMultiplier
    }

    private fun getMOVOne(pointDifferential: Int, winningTeamElo: Int, losingTeamElo: Int): Double {
        //Margin of Victory Multiplier = LN(ABS(PD)+1) * (2.2/((ELOW-ELOL)*.001+2.2))
        return kotlin.math.ln(abs(pointDifferential) + 1.0) * (2.2 / ((winningTeamElo - losingTeamElo) * .001 + 2.2))
    }

    private fun getMOVTwo(pointDifferential: Int, winningTeamElo: Int, losingTeamElo: Int): Double {
        val sqr = Math.pow(abs(pointDifferential) + 6.0, 0.8)
        return sqr / (7.5 + (.006*(winningTeamElo - losingTeamElo)))
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