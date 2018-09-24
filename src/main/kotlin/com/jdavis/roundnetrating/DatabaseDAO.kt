package com.jdavis.roundnetrating

import javafx.collections.ObservableList
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import tornadofx.*

class DatabaseDAO : Controller() {

    init {
        Database.connect("jdbc:h2:./database", driver = "org.h2.Driver")

        transaction {
            // print sql to std-out
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(Team)
        }
    }

    public fun getTeams() {
        var returnList: ObservableList<com.jdavis.roundnetrating.model.Team>

        val resultSet = transaction {
            Team.selectAll()
        }

        resultSet.forEach {
            println(it)
        }

    }

    public fun insertTeam(teamName: String) {
        transaction {
            val team = Team.insert {
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

    private object Player : Table() {
        val id = integer("id").autoIncrement().primaryKey()
        val name = varchar("name", 25)
    }

    object Team : Table() {
        val id = integer("id").autoIncrement().primaryKey()
        val name = varchar("name", 40)
        val wins = integer("wins")
        val losses = integer("losses")
        val swissPoints = integer("swisspoints")
        val pointDiff = integer("pointdifferential")
        val hadBye = bool("hadBye")
        val eloRating = integer("eloRating")
//        val playerOne = varchar("playerOneName", 25)
//                .references(Player.name)
//        val playerTwo = varchar("playerTwoName", 25)
//                .references(Player.name)
    }
}
