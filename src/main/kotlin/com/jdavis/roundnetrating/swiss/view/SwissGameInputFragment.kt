package com.jdavis.roundnetrating.swiss.view

import com.jdavis.roundnetrating.model.Game
import com.jdavis.roundnetrating.swiss.controller.SwissGameController
import javafx.beans.property.SimpleIntegerProperty
import javafx.scene.text.TextAlignment
import tornadofx.*

class SwissGameInputFragment : Fragment("Submit Game") {
    private val swissGameController: SwissGameController by inject()

    private var scoreOneProperty = SimpleIntegerProperty()
    private var scoreTwoProperty = SimpleIntegerProperty()

    // param gets passed when we open this fragment
    val game: Game by param()

    override val root = fieldset {
        label {
            text = game.teamOne.nameProperty().value + " vs. " + game.teamTwo.nameProperty().value
            textAlignment = TextAlignment.CENTER
            paddingAll = 10
        }

        hbox {
            label(game.teamOne.nameProperty().value)
            textfield(scoreOneProperty) {
                text = "15"
            }
        }

        hbox {
            label(game.teamTwo.nameProperty().value)
            textfield(scoreTwoProperty) {
                text = "13"
            }
        }

        button {
            text = "Submit"
            action {
                submitGame()
            }
        }
    }

    private fun submitGame() {
        if (isGameValid()) {
            game.scoreOne = scoreOneProperty.value
            game.scoreTwo = scoreTwoProperty.value
            game.round = game.teamOne.wins + game.teamOne.losses + 1
            swissGameController.submitGame(game)
            close()
        }
    }

    private fun isGameValid(): Boolean {
        return scoreOneProperty.value != scoreTwoProperty.value
    }
}
