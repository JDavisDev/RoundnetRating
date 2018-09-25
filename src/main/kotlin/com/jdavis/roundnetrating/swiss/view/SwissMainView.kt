package com.jdavis.roundnetrating.swiss.view

import com.jdavis.roundnetrating.DatabaseDAO
import com.jdavis.roundnetrating.model.Game
import com.jdavis.roundnetrating.model.Team
import com.jdavis.roundnetrating.swiss.controller.SwissScheduleController
import com.jdavis.roundnetrating.swiss.model.SwissGameData
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.control.TabPane
import javafx.scene.paint.Color
import javafx.stage.StageStyle
import tornadofx.*

class SwissMainView : View("Swiss") {


    override val root = tabpane {
        tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE

        tab("Teams") {
            add<StandingsTab>()
        }
        tab("Schedule") {
            add<ScheduleTab>()
        }
    }
}

class StandingsTab : View() {
    private val dbController: DatabaseDAO by inject()
    private var teamList: ObservableList<Team>
    private val newTeamInput = SimpleStringProperty()

    init {
        teamList = FXCollections.observableList(dbController.teamList)
    }

    override val root =
            form {
                button("Back") {
                    action {
                        goHome()
                    }
                }
                fieldset {
                    field("New Team: ") {
                        textfield(newTeamInput)
                    }

                    button("Add Team") {
                        action {
                            if (newTeamInput.value != null && newTeamInput.value.isNotEmpty()) {
                                dbController.insertTeam(newTeamInput.value)
                                teamList.add(
                                        Team(teamList.size + 1, newTeamInput.value, 1500,
                                                null, null, 0, 0,
                                                0, 0, false))
                            }
                            newTeamInput.value = ""
                        }
                    }

                    tableview(teamList) {
                        column("ID", Team::idProperty)
                        column("Name", Team::nameProperty)
                        column("Wins", Team::winsProperty)
                        column("Losses", Team::lossesProperty)
                        column("Points", Team::swissPointsProperty)
                        column("Point Differential", Team::pointDiffProperty)
                        column("ELO Rating", Team::eloRatingProperty)
                        column("Bye", Team::hadByeProperty)
                        columnResizePolicy = SmartResize.POLICY
                    }
                }
            }

    private fun goHome() {
        //close()
        //replaceWith(MainView::class)
    }
}

class ScheduleTab : View() {
    private val swissGameData: SwissGameData by inject()
    private val gameList: ObservableList<Game>
    private val swissScheduleController: SwissScheduleController by inject()

    init {
        gameList = FXCollections.observableList(swissGameData.getGamesInRound(1))
    }

    /**
     * Let's use an observable list here that updates like the other views
     * just an observable list of games and bind it to a property here or something?
     */
    override val root = form {
        fieldset {

            button("Generate Round") {
                action {
                    swissScheduleController.generateMatchups()
                }
            }

            // swiss rounds
            for (round in swissGameData.gameList.keys) {
                label("Round " + round.toString())

                hbox(20) {
                    for (game in gameList) {
                        if (game.round != 0 && game.round == round) {
                            button {
                                text = game.teamOne.nameProperty().value + " vs. " + game.teamTwo.nameProperty().value
                                action {
                                    openGameInputFragment(game)
                                }

                                paddingAll = 10.0

                                if (game.isReported) {
                                    style {
                                        backgroundColor += Color.GREEN
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun openGameInputFragment(game: Game) {
        find<SwissGameInputFragment>(SwissGameInputFragment::game to game).openModal(stageStyle = StageStyle.DECORATED)
    }
}