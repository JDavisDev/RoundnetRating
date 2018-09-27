package com.jdavis.roundnetrating.swiss.view

import com.jdavis.roundnetrating.DatabaseDAO
import com.jdavis.roundnetrating.model.Game
import com.jdavis.roundnetrating.model.Team
import com.jdavis.roundnetrating.swiss.controller.SwissGameController
import com.jdavis.roundnetrating.swiss.controller.SwissScheduleController
import com.jdavis.roundnetrating.swiss.model.SwissGameData
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.control.TabPane
import javafx.scene.input.KeyCode
import javafx.stage.StageStyle
import tornadofx.*

/**
 * Main View Screen
 */
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

/**
 * Standings Tab View
 */

class StandingsTab : View() {
    private val swissGameController: SwissGameController by inject()
    private val dbController: DatabaseDAO by inject()
    private val swissGameData: SwissGameData by inject()
    private var teamList: ObservableList<Team>
    private val newTeamInput = SimpleStringProperty()

    private val teamOneScoreInput = SimpleIntegerProperty()
    private val teamTwoScoreInput = SimpleIntegerProperty()
    private val teamOneProperty = SimpleObjectProperty<Team>()
    private val teamTwoProperty = SimpleObjectProperty<Team>()

    init {
        swissGameData.getAllTeams()
        teamList = FXCollections.observableList(swissGameData.teamsList)
    }

    override val root =
            form {
                fieldset {
                    field("New Team: ") {
                        textfield(newTeamInput) {
                            setOnKeyPressed {
                                if (it.code == KeyCode.ENTER) {
                                    addPlayer()
                                }
                            }
                        }
                    }
                    button("Add Team") {
                        action {
                            addPlayer()
                        }
                        paddingAll = 15.0
                    }

                    form {
                        fieldset {
                            field("Team One") {
                                combobox(teamOneProperty, teamList) {
                                    cellFormat {
                                        text = it.name
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
                                }
                            }

                            field("Team Two Score") {
                                textfield(teamTwoScoreInput)
                            }

                            button {
                                text = "Submit"
                                action {
                                    submitGame()
                                }
                                paddingAll = 15.0
                            }
                        }
                    }

                    tableview(teamList) {
                        column("ID", Team::idProperty)
                        column("Name", Team::nameProperty).contentWidth(5.0)
                        column("Wins", Team::winsProperty)
                        column("Losses", Team::lossesProperty)
                        column("Points", Team::swissPointsProperty)
                        column("Point Differential", Team::pointDiffProperty)
                        column("ELO Rating", Team::eloRatingProperty)
                        column("Bye", Team::hadByeProperty)
                        smartResize()
                    }
                }
            }

    /**
     * action methods from UI actions
     */
    private fun addPlayer() {
        if (newTeamInput.value != null && newTeamInput.value.isNotEmpty()) {
            dbController.insertTeam(newTeamInput.value)
            teamList.add(
                    Team(teamList.size + 1, newTeamInput.value, 1500,
                            null, null, 0, 0,
                            0, 0, false))
            newTeamInput.value = ""
        }
    }

    private fun submitGame() {
        if (isGameValid()) {
            val teamOne = teamOneProperty.value
            val teamTwo = teamTwoProperty.value
            val scoreOne = teamOneScoreInput.value
            val scoreTwo = teamTwoScoreInput.value
            val round = teamOne.wins + teamTwo.losses + 1

            val game = Game(1, round, teamOne.name, scoreOne, teamTwo.name, scoreTwo, true)
            swissGameController.submitGame(game)
            resetForm()
            alert(Alert.AlertType.NONE, "Success", "Game submitted.", ButtonType.OK)
        } else {
            alert(Alert.AlertType.ERROR, "Error", "Check that players and scores are unique.", ButtonType.OK)
        }
    }

    private fun isGameValid(): Boolean {
        return teamOneScoreInput.value != teamTwoScoreInput.value && (teamOneProperty.value != teamTwoProperty.value)
    }

    private fun resetForm() {
        teamOneScoreInput.value = 0
        teamTwoScoreInput.value = 0

        teamOneProperty.set(null)
        teamTwoProperty.set(null)
    }

    private fun goHome() {
    }
}


/**
 * Round Games/Schedule Tab View
 */
class ScheduleTab : View() {
    private val swissGameData: SwissGameData by inject()
    private val gameList: ObservableList<Game>
    private val swissScheduleController: SwissScheduleController by inject()

    init {
        swissGameData.getAllGames()
        gameList = FXCollections.observableList(swissGameData.gamesList)
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

            tableview(gameList) {
                column("ID", Game::idProperty)
                column("Round", Game::roundProperty)
                column("Team One", Game::teamOneProperty).contentWidth(5.0)
                column("Score One", Game::scoreOneProperty)
                column("Team Two", Game::teamTwoProperty).contentWidth(5.0)
                column("Score Two", Game::scoreTwoProperty)
                column("Reported", Game::isReportedProperty)
                smartResize()
                onUserSelect {
                    openGameInputFragment(it)
                }
            }
        }
    }

    /**
     * UI Action method
     */
    private fun openGameInputFragment(game: Game) {
        if (game.isReported) return
        find<SwissGameInputFragment>(SwissGameInputFragment::game to game).openModal(stageStyle = StageStyle.DECORATED)
    }
}

class GamesTab : View() {
    private val dbController: DatabaseDAO by inject()
    private val swissGameController: SwissGameController by inject()

    val teamOneScoreInput = SimpleIntegerProperty()
    val teamTwoScoreInput = SimpleIntegerProperty()
    val teamOneProperty = SimpleObjectProperty<Team>()
    val teamTwoProperty = SimpleObjectProperty<Team>()
    var teamList: ObservableList<Team>

    init {
        dbController.getTeams()
        teamList = FXCollections.observableList(dbController.teamList)
    }

    override val root = vbox {
        setMinSize(400.0, 1200.0)

        form {
            fieldset {
                field("Team One") {
                    combobox(teamOneProperty, teamList) {
                        cellFormat {
                            text = it.name
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
                    }
                }

                field("Team Two Score") {
                    textfield(teamTwoScoreInput)
                }

                button {
                    text = "Submit"
                    action {
                        submitGame()
                    }
                }
            }
        }
    }

    private fun submitGame() {
        if (isGameValid()) {
            val teamOne = teamOneProperty.value
            val teamTwo = teamTwoProperty.value
            val scoreOne = teamOneScoreInput.value
            val scoreTwo = teamTwoScoreInput.value
            val round = teamOne.wins + teamTwo.losses + 1

            val game = Game(1, round, teamOne.name, scoreOne, teamTwo.name, scoreTwo, true)
            swissGameController.submitGame(game)
            resetForm()
            alert(Alert.AlertType.NONE, "Success", "Game submitted.", ButtonType.OK)
        } else {
            alert(Alert.AlertType.ERROR, "Error", "Check that players and scores are unique.", ButtonType.OK)
        }
    }

    private fun isGameValid(): Boolean {
        return teamOneScoreInput.value != teamTwoScoreInput.value && (teamOneProperty.value.name != teamTwoProperty.value.name)
    }

    private fun resetForm() {
        teamOneScoreInput.value = 0
        teamTwoScoreInput.value = 0

        teamOneProperty.set(null)
        teamTwoProperty.set(null)
    }
}