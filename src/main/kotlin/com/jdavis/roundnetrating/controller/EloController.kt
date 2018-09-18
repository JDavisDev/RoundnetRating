package com.jdavis.roundnetrating.controller

import main.model.Match
import main.model.Player
import main.model.Team
import tornadofx.*
import java.lang.Math.abs
import kotlin.math.roundToInt

class EloController : Controller() {

    val RATING_FACTOR_K = 32
    var teamOneWinProbability: Double = 0.0
    var ratingDelta = 0
    lateinit var playerList: MutableList<Player>

    fun updateEloOfMatch(match: Match) {
        match.teamOne.eloRating = getTeamEloRating(match.teamOne)
        match.teamTwo.eloRating = getTeamEloRating(match.teamTwo)

        val winningTeam = getWinningTeam(match)
        val losingTeam = getLosingTeam(match)

        teamOneWinProbability = getTeamOneWinProbability(match.teamOne.eloRating, match.teamTwo.eloRating)
        val marginOfVictoryMultiplier = getMarginOfVictoryModifier(abs(match.scoreOne - match.scoreTwo), winningTeam.eloRating, losingTeam.eloRating)
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

    private fun getTeamOneWinProbability(teamOneRating: Int, teamTwoRating: Int): Double {
        // Probability of teamOne winning = 1 / (10^(-ELODIFF/400) + 1)
        //return (teamOneRating.toDouble() / (teamOneRating + teamTwoRating).toDouble())
        return 1 / (Math.pow(10.0, -(teamOneRating.toDouble() - teamTwoRating.toDouble()) / 400) + 1)
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
        return RATING_FACTOR_K * teamOneWinProbability * marginOfVictoryMultiplier
    }

    private fun getMarginOfVictoryModifier(pointDifferential: Int, winningTeamElo: Int, losingTeamElo: Int): Double {
        //Margin of Victory Multiplier = LN(ABS(PD)+1) * (2.2/((ELOW-ELOL)*.001+2.2))
        return kotlin.math.ln(abs(pointDifferential.toDouble()) + 1) * (2.2 / ((winningTeamElo - losingTeamElo) * .001 + 2.2))
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