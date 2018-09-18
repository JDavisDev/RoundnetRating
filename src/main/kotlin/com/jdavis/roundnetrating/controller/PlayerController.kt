package com.jdavis.roundnetrating.controller

import main.model.Player
import java.io.File
import tornadofx.*
import java.time.Instant
import java.util.*
import kotlin.io.*

class PlayerController : Controller() {

    val filename = "./PlayerDatabase.txt"
    var dbFile: File = File(filename)

    init {
        createTextDatabase()
    }

    fun addPlayer(name: String) {
        // write player to db
    }

    fun getPlayersFromDb() {

    }

    private fun createTextDatabase() {
        dbFile.writeText("Players : Updated " + Date.from(Instant.now()) + "\n")
    }

    private fun addPlayerToDb() {

    }

    private fun getPlayerFromDb() {

    }

    private fun updatePlayerEloInDb() {

    }

    private fun writeAllPlayersToDb(list: MutableList<Player>) {
        File(filename).printWriter().use { out ->
            list.map { "${it.id}, ${it.name}, ${it.eloRating}" }
                    .forEach { out.println(it) }
        }
    }

    private fun sortPlayersByElo() {

    }
}