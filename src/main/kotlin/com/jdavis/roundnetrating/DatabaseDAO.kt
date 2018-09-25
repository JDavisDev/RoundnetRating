package com.jdavis.roundnetrating

import com.jdavis.roundnetrating.model.Game
import com.jdavis.roundnetrating.model.Team
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import tornadofx.*

class DatabaseDAO : Controller() {

    var teamList: MutableList<com.jdavis.roundnetrating.model.Team>

    init {
        Database.connect("jdbc:h2:./database", driver = "org.h2.Driver")

        transaction {
            // print sql to std-out
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(Team)
            SchemaUtils.create(Game)
        }

        teamList = getTeams()
    }

    fun getTeams(): MutableList<com.jdavis.roundnetrating.model.Team> {
        var returnList = mutableListOf<com.jdavis.roundnetrating.model.Team>()

        transaction {
            val resultSet = Team.selectAll()
            resultSet.forEach {
                returnList.add(
                        Team(it[Team.id],
                                it[Team.name],
                                it[Team.eloRating],
                                null,
                                null,
                                it[Team.wins],
                                it[Team.losses],
                                it[Team.swissPoints],
                                it[Team.pointDiff],
                                it[Team.hadBye])
                )
            }
        }

        teamList = returnList
        return returnList
    }

    fun insertTeam(teamName: String) {
        transaction {
            Team.insert {
                it[name] = teamName
                it[eloRating] = 1500
                it[wins] = 0
                it[losses] = 0
                it[swissPoints] = 0
                it[hadBye] = false
                it[pointDiff] = 0
            }
        }
    }

    fun insertGame(game: com.jdavis.roundnetrating.model.Game) {
        transaction {
            Game.insert {
                it[teamOneName] = game.teamOne.name
                it[teamTwoName] = game.teamTwo.name
                it[isReported] = game.isReported
                it[round] = game.round
                it[scoreOne] = game.scoreOne
                it[scoreTwo] = game.scoreTwo
            }
        }
    }

    fun getAllGames(): MutableList<com.jdavis.roundnetrating.model.Game> {
        val returnList = mutableListOf<com.jdavis.roundnetrating.model.Game>()

        transaction {
            val resultSet = Game.selectAll()
            resultSet.forEach {
                returnList.add(
                        Game(it[Game.id],
                                it[Game.round],
                                getTeamByName(it[Game.teamOneName]),
                                it[Game.scoreOne],
                                getTeamByName(it[Game.teamTwoName]),
                                it[Game.scoreTwo]))
            }
        }

        return returnList
    }

    fun updateTeam(teamName: String, team: com.jdavis.roundnetrating.model.Team) {
        Team.update({ Team.name eq teamName }) {
            it[name] = teamName
            it[eloRating] = team.eloRating
            it[wins] = team.wins
            it[losses] = team.losses
            it[swissPoints] = team.swissPoints
            it[hadBye] = team.hadBye
            it[pointDiff] = team.pointDiff
        }
    }

    private fun getTeamByName(name: String): com.jdavis.roundnetrating.model.Team {
        for (team in teamList) {
            if (team.name == name) {
                return team
            }
        }

        return Team()
    }

    private object Player : Table() {
        val id = integer("id").autoIncrement()
        val name = varchar("name", 25).primaryKey()
    }

    object Team : Table() {
        val id = integer("id").autoIncrement()
        val name = varchar("name", 40).primaryKey()
        val wins = integer("wins")
        val losses = integer("losses")
        val swissPoints = integer("swisspoints")
        val pointDiff = integer("pointdifferential")
        val hadBye = bool("hadBye")
        val eloRating = integer("eloRating")
    }

    object Game : Table() {
        val id = integer("id").autoIncrement().primaryKey()
        val teamOneName = varchar("teamOneName", 25).references(Team.name)
        val teamTwoName = varchar("teamTwoName", 25).references(Team.name)
        val isReported = bool("isReported")
        val round = integer("round")
        val scoreOne = integer("scoreOne")
        val scoreTwo = integer("scoreTwo")
    }
}
