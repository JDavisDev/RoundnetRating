package com.jdavis.roundnetrating.elo.controller

import com.jdavis.roundnetrating.DatabaseDAO
import com.jdavis.roundnetrating.model.Game
import com.jdavis.roundnetrating.model.Team
import tornadofx.*
import java.lang.Math.abs
import kotlin.math.roundToInt

class EloController : Controller() {

    private val db: DatabaseDAO by inject()
    private val ratingScaleK = 32
    private var ratingDelta = 0
    private var teamOneExpectedWinRate: Double = 0.5
    private var teamList = mutableListOf<Team>()
    private var teamOne = Team()
    private var teamTwo = Team()

    init {
        teamList = db.getTeams()
    }
    fun updateEloOfMatch(game: Game) {
        teamList = db.getTeams()

        teamOne = getTeamByName(game.teamOne)
        teamTwo = getTeamByName(game.teamTwo)
        teamOne.eloRating = getTeamEloRating(teamOne)
        teamTwo.eloRating = getTeamEloRating(teamTwo)

        val winningTeam = getWinningTeam(game)
        val losingTeam = getLosingTeam(game)

        setTeamExpectedWinRate(teamOne.eloRating, teamTwo.eloRating)
        val marginOfVictoryMultiplier =
                getMOVOne(abs(game.scoreOne - game.scoreTwo),
                        getTeamByName(winningTeam).eloRating,
                        getTeamByName(losingTeam).eloRating)

        ratingDelta = getRatingDelta(marginOfVictoryMultiplier).roundToInt()

        updateTeamELO(winningTeam, losingTeam)
    }

    private fun getTeamEloRating(team: Team) : Int {
        return team.eloRating
        //return (team.playerOne.eloRating + team.playerTwo.eloRating) / 2
    }

    private fun setTeamExpectedWinRate(teamOneRating: Int, teamTwoRating: Int) {
        // Probability of teamOne winning = 0...1 / (10^(ELODIFF/400) + 1)
        val teamOneVal = Math.pow(10.0, (teamOneRating.toDouble()) / 400.0)
        val teamTwoVal = Math.pow(10.0, (teamTwoRating.toDouble()) / 400.0)
        teamOneExpectedWinRate = teamOneVal / (teamOneVal + teamTwoVal)
    }

    private fun getWinningTeam(game: Game): String {
        return when {
            game.scoreOne > game.scoreTwo -> game.teamOne
            else -> game.teamTwo
        }
    }

    private fun getLosingTeam(game: Game): String {
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

    private fun updateTeamELO(winningTeam: String, losingTeam: String) {
        val one = getTeamByName(winningTeam)
        val two = getTeamByName(losingTeam)

        one.eloRating += ratingDelta
        two.eloRating -= ratingDelta

        db.updateTeam(one.name, one)
        db.updateTeam(two.name, two)

//        updatePlayerElo(one, ratingDelta)
//        updatePlayerElo(two, -ratingDelta)
    }

    private fun updatePlayerElo(team: Team, ratingDelta: Int) {
//        team.playerOne.eloRating += ratingDelta
//        team.playerTwo.eloRating += ratingDelta
    }

    private fun getTeamByName(name: String): Team {
        for (team in teamList) {
            if (team.name.equals(name, true)) {
                return team
            }
        }

        return Team()
    }
}