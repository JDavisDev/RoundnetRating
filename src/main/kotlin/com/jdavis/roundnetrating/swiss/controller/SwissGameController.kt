package com.jdavis.roundnetrating.swiss.controller

import com.jdavis.roundnetrating.model.Game
import tornadofx.*

class SwissGameController : Controller() {
    public fun submitGame(game: Game) {
        // save to our db
        game.isReported = true
    }
}