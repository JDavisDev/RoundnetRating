package com.jdavis.roundnetrating.view

import com.jdavis.roundnetrating.controller.EloController
import com.jdavis.roundnetrating.controller.PlayerController
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import main.model.Match
import main.model.Player
import main.model.Team
import tornadofx.*
import java.util.*

class MainView : View("Hello TornadoFX") {

    private val eloController: EloController by inject()
    private val playerController: PlayerController by inject()
    private val newPlayerNameInput = SimpleStringProperty()
    private val teamOneScoreInput = SimpleIntegerProperty()
    private val teamTwoScoreInput = SimpleIntegerProperty()

    private var teamList = FXCollections.observableList(mutableListOf(
            Player(1, "Jordan Davis", 1500),
            Player(2, "Andrew Leasau", 1500),
            Player(3, "Tommy Adesso", 1500),
            Player(4, "Kane Rickman", 1500),
            Player(5, "Ryan Quintana", 1500)
    ))


    override val root = vbox {
        form {
            fieldset {
                field("New Player: ") {
                    textfield(newPlayerNameInput)
                }

                button("Add Player") {
                    action {
                        if(newPlayerNameInput.value != null && newPlayerNameInput.value.isNotEmpty()) {
                            playerController.addPlayer(newPlayerNameInput.value)
                            teamList.add(Player(teamList.count()+1, newPlayerNameInput.value, 1500))
                        }
                        newPlayerNameInput.value = ""
                    }
                }
            }
        }

        form {
            fieldset {
                field("Player One") {

                    combobox<Player> {
                        items = teamList
                    }
                }
                field("Player Two") {
                    combobox<Player> {
                        items = teamList
                    }
                }

                field("Game Score") {
                    textfield(teamOneScoreInput)
                }

                field("Player Three") {
                    combobox<Player> {
                        items = teamList
                    }
                }
                field("Player Four") {
                    combobox<Player> {
                        items = teamList
                    }
                }

                field("Game Score") {
                    textfield(teamTwoScoreInput)
                }


                button("Save Game") {
                    useMaxWidth = true
                    action {
                        val teamOne = Team(1)
                        teamOne.playerOne = teamList[0]
                        teamOne.playerTwo = teamList[1]
                        val scoreOne = 21
                        val teamTwo = Team(2)
                        teamTwo.playerOne = teamList[2]
                        teamTwo.playerTwo = teamList[3]
                        val scoreTwo = 19

                        val match = Match(1, teamOne, scoreOne, teamTwo, scoreTwo)
                        eloController.updateEloOfMatch(match)
                    }
                }
            }
        }

        tableview(teamList) {
            column("ID",Player::idProperty)
            column("Name", Player::nameProperty)
            column("ELO Rating", Player::eloRatingProperty)
        }
    }
}