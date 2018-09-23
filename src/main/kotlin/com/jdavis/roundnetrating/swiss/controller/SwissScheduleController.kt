package com.jdavis.roundnetrating.swiss.controller

import com.jdavis.roundnetrating.model.Team
import com.jdavis.roundnetrating.swiss.model.SwissGameData
import main.model.Game
import tornadofx.*

class SwissScheduleController : Controller() {

    var swissGameData = SwissGameData()
    var floatingTeamsList = mutableListOf<Team>()
    var teamList = setupMockTeamList()
    var round: Int = 1

    fun generateMatchups() {
        sortTeams(teamList)
        beginSim()
    }

    private fun setupMockTeamList(): MutableList<Team> {
        // change this later to get teams from db
        var teamList: MutableList<Team> = mutableListOf()

        var teamOne = Team(1)
        teamOne.swissPoints = 1

        var teamTwo = Team(2)
        teamTwo.swissPoints = 1

        var twoHalf = Team(5)
        twoHalf.swissPoints = 1
        twoHalf.eloRating = 1800

        var teamThree = Team(3)
        teamThree.swissPoints = 0
        teamThree.eloRating = 1800

        var teamFour = Team(4)
        teamThree.swissPoints = 0
        teamThree.eloRating = 1800

        teamList.add(teamOne)
        teamList.add(teamTwo)
        teamList.add(teamThree)
        teamList.add(teamFour)
        teamList.add(twoHalf)

        return teamList
    }

    private fun sortTeams(list: MutableList<Team>) {
        list.sortByDescending {
            it.swissPoints
        }
    }

    private fun beginSim() {
        updateTeamPoints()
        groupTeamsBySwissPoints()
        resetValues()
        round += 1
    }

    private fun resetValues() {
        floatingTeamsList.clear()

    }

    private fun updateTeamPoints() {
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

            createRoundMatchups(round, tempList)
        }

        createMatchupsFromFloatingTeams()
    }

    private fun createRoundMatchups(round: Int, list: MutableList<Team>) {
        // odd number?
        if (list.size % 2 != 0 || list.size == 1) {
            // add the middle most team to be the floater
            // this way, top and bottom seeds of this list play each other
            val middleTeam = list[list.size / 2]
            floatingTeamsList.add(middleTeam)
            list.remove(middleTeam)
        }

        for (index in 0 until list.size / 2) {
            val id = Integer.parseInt(round.toString() + (index + 1).toString())
            val teamOne = list[index]
            val teamTwo = list[list.size / 2 + index]

            swissGameData.insertGame(round, createNewGame(id, teamOne, teamTwo))
        }
    }

    // floating team list should NEVER be odd..
    private fun createMatchupsFromFloatingTeams() {
        sortTeams(floatingTeamsList)

        for (index in 0 until floatingTeamsList.size - 1) {
            val teamOne = floatingTeamsList[index]
            val teamTwo = floatingTeamsList[index + 1]
            val id = Integer.parseInt(round.toString() + (index + 1).toString() + "0")

            swissGameData.insertGame(round, createNewGame(id, teamOne, teamTwo))
        }
    }

    private fun createNewGame(id: Int, teamOne: Team, teamTwo: Team): Game {
        return Game(id, teamOne, 0, teamTwo, 0)
    }

    private fun giveTeamBye(team: Team) {
        team.hadBye = true
        team.swissPoints += 1
    }
}