package com.jdavis.roundnetrating

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class DatabaseDAO {

    init {

        Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver")

        transaction {
            // print sql to std-out
            addLogger(StdOutSqlLogger)

            SchemaUtils.create(Player)

            // insert new city. SQL: INSERT INTO Cities (name) VALUES ('St. Petersburg')
            val jordan = Player.insert {
                it[name] = "Jordan"
            } get Player.id

            // 'select *' SQL: SELECT Cities.id, Cities.name FROM Cities
            println("Players: ${Player.selectAll()}")
        }
    }

    object Player : Table() {
        val id = integer("id").autoIncrement().primaryKey()
        val name = varchar("name", 25)
    }

    object Team : Table() {
        val id = integer("id").autoIncrement().primaryKey()
        val name = varchar("name", 40)
        val wins = integer("wins")
        val losses = integer("losses")
        val swissPoints = integer("swisspoints")
        val playerOne = varchar("playerOneName", 25)
                .references(Player.name)
        val playerTwo = varchar("playerTwoName", 25)
                .references(Player.name)
    }
}
