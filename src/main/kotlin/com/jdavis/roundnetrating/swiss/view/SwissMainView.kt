package com.jdavis.roundnetrating.swiss.view

import com.jdavis.roundnetrating.elo.controller.DatabaseController
import com.jdavis.roundnetrating.model.Team
import com.jdavis.roundnetrating.swiss.controller.SwissGameController
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.control.TabPane
import javafx.stage.StageStyle
import main.model.Game
import tornadofx.*

class SwissMainView : View("Swiss") {


    override val root = tabpane {
        tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE

        tab("Teams") {
            vbox {
                button("Button 1")
                button("Button 2")
            }
        }
        tab("Schedule") {
            add<ScheduleTab>()
        }
        tab("Standings") {
            //            tableview(teamList) {
//                column("ID", Player::idProperty)
//                column("Name", Player::nameProperty)
//                column("ELO Rating", Player::eloRatingProperty)
//            }
        }
    }
}

class ScheduleTab : View() {

    private val swissGameController: SwissGameController by inject()
    private val dbController: DatabaseController by inject()
    private var teamList: ObservableList<Team>

    private val teamOneScoreInput = SimpleIntegerProperty()
    private val teamTwoScoreInput = SimpleIntegerProperty()
    private val teamOneProperty = SimpleObjectProperty<Team>()
    private val teamTwoProperty = SimpleObjectProperty<Team>()

    init {
        teamList = FXCollections.observableList(dbController.teamList)
    }

    override val root = form {
        fieldset {
            field("Team One") {
                combobox(teamOneProperty, teamList) {
                    cellFormat {
                        text = it.name
                    }
                    teamOneProperty.onChange {
                        //selectedPlayerOne = value
                    }
                }
            }

            field("Team One Score") {
                textfield(teamOneScoreInput)
            }

            field("Team Two") {
                combobox(teamTwoProperty, teamList) {
                    cellFormat {
                        text = it.name
                    }

                    teamTwoProperty.onChange {
                        //selectedPlayerTwo = value
                    }
                }
            }

            field("Team Two Score") {
                textfield(teamOneScoreInput)
            }

            button {
                text = "Submit Game"
                action {
                    openGameInputFragment()
                }
            }
        }
    }

    private fun openGameInputFragment() {
        find<SwissGameInputFragment>().openModal(stageStyle = StageStyle.DECORATED)
    }

    private fun submitGame() {
        if (true) {
            val teamOne = teamOneProperty.value
            val scoreOne = teamOneScoreInput.value
            val teamTwo = teamTwoProperty.value

            val scoreTwo = teamTwoScoreInput.value
            val game = Game(1, teamOne, scoreOne, teamTwo, scoreTwo)
            //eloController.updateEloOfMatch(match)
            dbController.writeAllPlayersToDb()
            swissGameController.submitGame(game)
            teamOneScoreInput.set(21)
            teamTwoScoreInput.set(0)
        } else {
            // show error dialog or something
        }
    }
}