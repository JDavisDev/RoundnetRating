package com.jdavis.roundnetrating.controller

import main.model.Player
import tornadofx.*
import java.io.File

class DatabaseController : Controller() {

    val filename = "./PlayerDatabase.txt"
    var dbFile: File = File(filename)

    public var playerList = mutableListOf<Player>()

    init {
        createTextDatabase()
        getAllPlayersFromDb()
    }

    private fun createTextDatabase() {
        dbFile.createNewFile()
    }

    fun addPlayerToDb(player: Player) {
        // write player to db
        dbFile.appendText((playerList.size + 1).toString() + "," + player.name + "," + "1500,")
        playerList.add(player)
    }

    fun addPlayerToDb(name: String) {
        // write player to db
        val player = Player(playerList.size + 1, name, 1500)
        dbFile.appendText((playerList.size + 1).toString() + "," + name + "," + "1500,\n")
        playerList.add(player)

        writeAllPlayersToDb()
    }

    fun getAllPlayersFromDb() {
        playerList.clear()

        val textParams = dbFile.readText().split(",")
                .map {
                    it.trim()
                }

        for (index in textParams.indices step 3) {
            if (textParams.indices.count() >= index + 2) {
                val player = Player(Integer.parseInt(textParams[index]), textParams[index + 1], Integer.parseInt(textParams[index + 2]))
                playerList.add(player)
            }
        }
    }

    private fun getPlayerFromDb() {

    }

    private fun updatePlayerEloInDb() {

    }

    public fun writeAllPlayersToDb() {
        dbFile.writeText("")
        dbFile.printWriter().use { out ->
            playerList.map { "${it.id}, ${it.name}, ${it.eloRating}," }
                    .forEach { out.println(it) }
        }
    }

    private fun sortPlayersByElo() {

    }


}