package com.jdavis.roundnetrating.swiss.view

import com.jdavis.roundnetrating.model.Game
import javafx.scene.text.TextAlignment
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
            textfield("0")
        }

        hbox {
            label(game.teamTwo.nameProperty().value)
            textfield("0")
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
