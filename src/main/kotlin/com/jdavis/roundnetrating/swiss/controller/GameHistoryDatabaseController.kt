package com.jdavis.roundnetrating.swiss.controller

import com.jdavis.roundnetrating.model.Game
import java.io.File

class GameHistoryDatabaseController {
    private val filename = "./GameHistoryDatabase.txt"
    var gameDbFile: File = File(filename)

    public var gameList = mutableListOf<Game>()

    init {
        createTextDatabase()
        getAllGamesFromDb()
    }

    private fun createTextDatabase() {
        gameDbFile.createNewFile()
    }

    fun addGameToDb(game: Game) {
        // write player to db
        val game = Game(game.id, 1, game.teamOne, game.scoreOne, game.teamTwo, game.scoreTwo)
        gameDbFile.appendText((gameList.size + 1).toString() + "," + game.id.toString() + "," + "1500\n")
        gameList.add(game)

        writeAllGamesToDb()
    }

    fun getAllGamesFromDb() {
        gameList.clear()
        val textParams = mutableListOf<String>()
        val lines = gameDbFile.readLines()

        for (line in lines) {
            if (line.indices.count() > 2) {
                textParams.addAll(line.split(",")
                        .map {
                            it.trim()
                        })
            }
        }

        for (index in textParams.indices step 3) {
            if (textParams.indices.count() >= index + 2) {
//                val game = Game(Integer.parseInt(textParams[index]), textParams[index + 1], Integer.parseInt(textParams[index + 2]))
//                gameList.add(game)
            }
        }
    }

    private fun getPlayerFromDb() {

    }

    private fun updatePlayerEloInDb() {

    }

    public fun writeAllGamesToDb() {
        gameDbFile.writeText("")
        gameDbFile.printWriter().use { out ->
            gameList.map { "${it.id}, ${it.teamOne.name}, ${it.teamTwo.name}" }
                    .forEach { out.println(it) }
        }
    }
}