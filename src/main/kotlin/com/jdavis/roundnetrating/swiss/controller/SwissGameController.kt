package com.jdavis.roundnetrating.swiss.controller

import com.jdavis.roundnetrating.model.Game
import com.jdavis.roundnetrating.swiss.model.SwissGameData
import tornadofx.*

class SwissGameController : Controller() {

    private val swissGameData: SwissGameData by inject()

    fun submitGame(game: Game) {
        // save to our db
        game.isReported = true
        swissGameData.insertGame(game.round, game)
    }
}