package com.jdavis.roundnetrating.elo.controller

import tornadofx.*

class PlayerController : Controller() {

    private var dbController: DatabaseController = DatabaseController()

    fun addPlayer(name: String) {
        // write player to db
        dbController.addPlayerToDb(name)
    }

    fun getPlayerList() {

    }
}