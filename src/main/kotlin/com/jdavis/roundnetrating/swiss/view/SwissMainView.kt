package com.jdavis.roundnetrating.swiss.view

import com.jdavis.roundnetrating.elo.controller.DatabaseController
import com.jdavis.roundnetrating.model.Team
import com.jdavis.roundnetrating.swiss.controller.SwissGameController
import com.jdavis.roundnetrating.swiss.controller.SwissScheduleController
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
    private val gameList: ObservableList<Game>
    private val swissTest: SwissScheduleController by inject()

    private val teamOneScoreInput = SimpleIntegerProperty()
    private val teamTwoScoreInput = SimpleIntegerProperty()
    private val teamOneProperty = SimpleObjectProperty<Team>()
    private val teamTwoProperty = SimpleObjectProperty<Team>()

    init {
        swissTest.generateMatchups()
        gameList = FXCollections.observableList(swissTest.swissGameData.getGamesInRound(1))
    }

    override val root = form {
        fieldset {

            for (round in swissTest.swissGameData.gameList.keys) {
                label("Round " + round.toString())

                for (game in gameList) {
                    button {
                        text = game.teamOne.nameProperty().value + " vs. " + game.teamTwo.nameProperty().value
                        action {
                            openGameInputFragment(game)
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