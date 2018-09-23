package com.jdavis.roundnetrating.swiss.view

import javafx.scene.text.TextAlignment
import main.model.Game
import tornadofx.*

class SwissGameInputFragment : Fragment("Submit Game") {
    val game: Game by param()

    override val root = fieldset {
        label {
            text = game.teamOne.nameProperty().value + " vs. " + game.teamTwo.nameProperty().value
            textAlignment = TextAlignment.CENTER
            paddingAll = 10
        }

        hbox {
            label(game.teamOne.nameProperty().value)
            textfield(game.teamOne.nameProperty().value + " Score")
        }

        hbox {
            label(game.teamTwo.nameProperty().value)
            textfield(game.teamTwo.nameProperty().value + " Score")
        }

        button {
            text = "Submit"
            action {
                submitGame()
            }
        }
    }

    private fun submitGame() {
        close()
    }
}
