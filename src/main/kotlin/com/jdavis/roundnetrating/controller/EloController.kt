package com.jdavis.roundnetrating.controller

import main.model.Match
import main.model.Player
import main.model.Team
import tornadofx.*
import java.lang.Math.abs
import kotlin.math.roundToInt

class EloController : Controller() {

    val RATING_FACTOR_K = 50
    var teamOneWinProbability: Double = 0.0
    var ratingDelta = 0
    lateinit var playerList: MutableList<Player>

    fun main(args: Array<String>) {
        playerList = mutableListOf(Player(22, "Jordan", 1500))
        testELO()
    }

    fun testELO() {
        val match = createTestMatch()
        val winningTeam = getWinningTeam(match)
        val losingTeam = getLosingTeam(match)
        teamOneWinProbability = getTeamOneWinProbability(match.teamOne.eloRating, match.teamTwo.eloRating)
        val marginOfVictoryMultiplier = getMarginOfVictoryModifier(abs(match.scoreOne - match.scoreTwo), winningTeam.eloRating, losingTeam.eloRating)
        ratingDelta = getRatingDelta(marginOfVictoryMultiplier).roundToInt()

        updateELO(winningTeam, losingTeam)
    }

    private fun createTestMatch(): Match {
        val teamOne = Team(1)
        val teamTwo = Team(2)

        teamOne.eloRating = 1400
        teamTwo.eloRating = 1600

        val scoreOne = 19
        val scoreTwo = 21

        return Match(1, teamOne, scoreOne, teamTwo, scoreTwo)
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

    private fun updateELO(winningTeam: Team, losingTeam: Team) {
        winningTeam.eloRating += ratingDelta
        losingTeam.eloRating -= ratingDelta
    }

    private fun getRatingDelta(marginOfVictoryMultiplier: Double): Double {
        return RATING_FACTOR_K * teamOneWinProbability * marginOfVictoryMultiplier
    }

    private fun getMarginOfVictoryModifier(pointDifferential: Int, winningTeamElo: Int, losingTeamElo: Int): Double {
        //Margin of Victory Multiplier = LN(ABS(PD)+1) * (2.2/((ELOW-ELOL)*.001+2.2))
        return kotlin.math.ln(abs(pointDifferential.toDouble()) + 2) * (2.2 / ((winningTeamElo - losingTeamElo) * .001 + 2.2))
    }
}