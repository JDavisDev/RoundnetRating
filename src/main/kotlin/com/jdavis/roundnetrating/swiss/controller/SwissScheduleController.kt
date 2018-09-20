package com.jdavis.roundnetrating.swiss.controller

import com.jdavis.roundnetrating.model.Team
import main.model.Game

class SwissScheduleController {

    var roundGameList: MutableList<Game> = mutableListOf()
    var teamList: MutableList<Team> = setupMockTeamList()
    var round: Int = 1

    fun generateMatchups() {
        sortTeams()
        createRoundOneMatchups()
        beginSim()
    }

    fun setupMockTeamList(): MutableList<Team> {
        // change this later to get teams from db
        var teamList: MutableList<Team> = mutableListOf()

        var teamOne = Team(1)
        teamOne.swissPoints = 3

        var teamTwo = Team(2)
        teamTwo.swissPoints = 2

        var teamThree = Team(3)
        teamThree.swissPoints = 3
        teamThree.eloRating = 1800

        var teamFour = Team(4)
        teamThree.swissPoints = 0
        teamThree.eloRating = 1800

        teamList.add(teamOne)
        teamList.add(teamTwo)
        teamList.add(teamThree)
        teamList.add(teamFour)

        return teamList
    }

    fun sortTeams() {
        teamList.sortByDescending {
            it.swissPoints
        }
    }

    private fun createRoundOneMatchups() {
        for (index in 0 until teamList.size / 2) {
            val id = Integer.parseInt(round.toString() + (index + 1).toString())
            val game = Game(id, teamList[index], 0, teamList[teamList.size / 2 + index], 0)
            roundGameList.add(game)
        }
    }

    private fun beginSim() {
        teamList[0].swissPoints++
        teamList[1].swissPoints += 2
        createRoundOfMatchups()
    }

    private fun createRoundOfMatchups() {
        var roundTeamList = teamList

        for (index in 1..teamList.size / 2) {
            val teamOne = getTeamOne(roundTeamList)
            val teamTwo = getTeamTwo(roundTeamList, teamOne)

            if (teamTwo != null) {
                val id = Integer.parseInt(round.toString() + index.toString())
                val game = Game(id, teamOne, 0, teamTwo, 0)
                roundGameList.add(game)
                roundTeamList.remove(teamOne)
                roundTeamList.remove(teamTwo)
            } else {
                continue
            }
        }
    }

    private fun getTeamOne(roundTeamList: MutableList<Team>): Team {
        return roundTeamList[0]
    }

    private fun getTeamTwo(roundTeamList: MutableList<Team>, opponent: Team): Team? {
        var team = Team(-1)

        for (index in 0 until roundTeamList.size - 1) {
            if (roundTeamList[index] != opponent &&
                    roundTeamList[index].swissPoints == opponent.swissPoints) {
                team = roundTeamList[index]
            }
        }

        // team wasn't assigned yet, meaning no team is tied. pick someone else?
        if (team.id == -1) {
            return null
        }

        return team
    }
}