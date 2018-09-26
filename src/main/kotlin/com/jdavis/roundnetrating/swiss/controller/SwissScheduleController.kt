package com.jdavis.roundnetrating.swiss.controller

import com.jdavis.roundnetrating.DatabaseDAO
import com.jdavis.roundnetrating.model.Game
import com.jdavis.roundnetrating.model.Team
import com.jdavis.roundnetrating.swiss.model.SwissGameData
import tornadofx.*

class SwissScheduleController : Controller() {

    private val swissGameData: SwissGameData by inject()

    private val dbController: DatabaseDAO by inject()

    private var floatingTeamsList = mutableListOf<Team>()
    private var teamList = dbController.getTeams()
    private var round: Int = setRound()

    fun generateMatchups() {
        teamList = swissGameData.getAllTeams()

        if (teamList.size > 1) {
            setRound()
            sortTeams(teamList)
            groupTeamsBySwissPoints()
            resetValues()
        }
    }

    private fun setRound(): Int {
        round = 1
        for (team in teamList) {
            if (round < team.wins + team.losses + 1) {
                round = team.wins + team.losses + 1
            }
        }

        return round
    }

    private fun sortTeams(list: MutableList<Team>) {
        list.sortByDescending {
            it.swissPoints
        }
    }

    private fun resetValues() {
        floatingTeamsList.clear()
    }

    /**
     * This method essentially gathers all the players with the same swiss score into one list,
     * then creates matchups from that list
     * Need to handle odd numbers and check for not perfect match ups.
     * i guess if the create matchups method gets an odd, one team is set aside to match up with another
     */
    private fun groupTeamsBySwissPoints() {
        val roundTeamList = teamList.toMutableList()

        // odd number?
        // start at the back and give the worst team a bye who has yet to have one
        if (roundTeamList.size % 2 != 0) {
            for (index in roundTeamList.size downTo 0) {
                if (roundTeamList[index - 1].hadBye) {
                    continue
                } else {
                    giveTeamBye(roundTeamList[roundTeamList.size - 1])
                    roundTeamList.removeAt(roundTeamList.size - 1)
                    break
                }
            }
        }

        for (pointValue in round downTo -1) {
            val tempList = mutableListOf<Team>()

            for (team in roundTeamList) {
                if (team.swissPoints == pointValue) {
                    tempList.add(team)
                }
            }

            if (tempList.size > 0) {
                createRoundMatchups(tempList)
            }
        }

        if (floatingTeamsList.size > 0) {
            createMatchupsFromFloatingTeams()
        }
    }

    private fun createRoundMatchups(list: MutableList<Team>) {
        val middleTeam: Team?

        // we've already been thru and the top list found a floater, pair immediately
        if (floatingTeamsList.size > 0) {
            val team = floatingTeamsList[0]
            val teamTwo = list[0]
            floatingTeamsList.removeAt(0)
            list.removeAt(0)
            createRoundMatchups(mutableListOf(team, teamTwo))
        }

        // odd number?
        if (list.size % 2 != 0 || list.size == 1) {
            // add the middle most team to be the floater
            // this way, top and bottom seeds of this list play each other
            middleTeam = list[list.size / 2]
            floatingTeamsList.add(middleTeam)
            list.remove(middleTeam)

        }

        for (index in 0 until list.size / 2) {
            val teamOne = list[index]
            val teamTwo = list[list.size / 2 + index]

            if (isMatchupUnique(teamOne.name, teamTwo.name)) {
                swissGameData.insertGame(round, createNewGame(teamOne, teamTwo))
                swissGameData.insertGameIntoDb(createNewGame(teamOne, teamTwo))
                swissGameData.gamesList = swissGameData.getAllGames()
            }
        }
    }

    // floating team list should NEVER be odd..
    private fun createMatchupsFromFloatingTeams() {
        sortTeams(floatingTeamsList)

        for (index in 0 until floatingTeamsList.size - 1) {
            val teamOne = floatingTeamsList[index]
            val teamTwo = floatingTeamsList[index + 1]

            if (isMatchupUnique(teamOne.name, teamTwo.name)) {
                swissGameData.insertGame(round, createNewGame(teamOne, teamTwo))
                swissGameData.insertGameIntoDb(createNewGame(teamOne, teamTwo))
                swissGameData.gamesList = swissGameData.getAllGames()
            }
        }
    }

    private fun createNewGame(teamOne: Team, teamTwo: Team): Game {
        return Game(null, round, teamOne.name, 0, teamTwo.name, 0, false)
    }

    private fun giveTeamBye(team: Team) {
        team.hadBye = true
        team.swissPoints += 1
    }

    private fun isMatchupUnique(teamOne: String, teamTwo: String): Boolean {
        // check if they've played each other
        for (game in swissGameData.getAllGames()) {
            if ((game.teamOne == teamOne && game.teamTwo == teamTwo) ||
                    (game.teamTwo == teamOne && game.teamOne == teamTwo)) {
                return false
            }
        }

        return true
    }
}