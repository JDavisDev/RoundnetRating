package com.jdavis.roundnetrating

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

    }

    fun getAllGames() {

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
