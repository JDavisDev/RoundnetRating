package com.jdavis.roundnetrating.view

import com.jdavis.roundnetrating.controller.DatabaseController
import com.jdavis.roundnetrating.controller.EloController
import com.jdavis.roundnetrating.controller.PlayerController
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import main.model.Match
import main.model.Player
import main.model.Team
import tornadofx.*

class MainView : View("Hello TornadoFX") {

    private val dbController: DatabaseController by inject()
    private val eloController: EloController by inject()
    private val playerController: PlayerController by inject()
    private val newPlayerNameInput = SimpleStringProperty()
    private val teamOneScoreInput = SimpleIntegerProperty()
    private val teamTwoScoreInput = SimpleIntegerProperty()
    private val playerOneProperty = SimpleObjectProperty<Player>()
    private val playerTwoProperty = SimpleObjectProperty<Player>()
    private val playerThreeProperty = SimpleObjectProperty<Player>()
    private val playerFourProperty = SimpleObjectProperty<Player>()

    lateinit var selectedPlayerOne: Player
    lateinit var selectedPlayerTwo: Player
    lateinit var selectedPlayerThree: Player
    lateinit var selectedPlayerFour: Player
    private var playerList: ObservableList<Player>

    init {
        dbController.getAllPlayersFromDb()
        playerList = FXCollections.observableList(dbController.playerList)
    }

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
                            playerList.add(Player(playerList.count() + 1, newPlayerNameInput.value, 1500))
                        }
                        newPlayerNameInput.value = ""
                    }
                }
            }
        }

        form {
            fieldset {
                field("Player One") {
                    combobox(playerOneProperty, playerList) {
                        cellFormat {
                            text = it.name
                        }
                        playerOneProperty.onChange {
                            selectedPlayerOne = value
                        }
                    }
                }
                field("Player Two") {
                    combobox(playerTwoProperty, playerList) {
                        cellFormat {
                            text = it.name
                        }

                        playerTwoProperty.onChange {
                            selectedPlayerTwo = value
                        }
                    }
                }

                field("Game Score") {
                    textfield(teamOneScoreInput)
                }

                field("Player Three") {
                    combobox(playerThreeProperty, playerList) {
                        cellFormat {
                            text = it.name
                        }

                        playerThreeProperty.onChange {
                            selectedPlayerThree = value
                        }
                    }
                }
                field("Player Four") {
                    combobox(playerFourProperty, playerList) {
                        cellFormat {
                            text = it.name
                        }
                        playerFourProperty.onChange {
                            selectedPlayerFour = value
                        }
                    }
                }

                field("Game Score") {
                    textfield(teamTwoScoreInput)
                }


                button("Save Game") {
                    useMaxWidth = true
                    action {
                        submitGame()
                    }
                }
            }
        }

        tableview(playerList) {
            column("ID", Player::idProperty)
            column("Name", Player::nameProperty)
            column("ELO Rating", Player::eloRatingProperty)
        }
    }

    private fun submitGame() {
        if (isGameValid()) {
            val teamOne = Team(1)
            teamOne.playerOne = selectedPlayerOne
            teamOne.playerTwo = selectedPlayerTwo
            val scoreOne = teamOneScoreInput.value
            val teamTwo = Team(2)
            teamTwo.playerOne = selectedPlayerThree
            teamTwo.playerTwo = selectedPlayerFour
            val scoreTwo = teamTwoScoreInput.value
            val match = Match(1, teamOne, scoreOne, teamTwo, scoreTwo)
            eloController.updateEloOfMatch(match)
            dbController.writeAllPlayersToDb()
        } else {
            // show error dialog or something
        }
    }

    private fun isGameValid(): Boolean {
        return true
    }
}