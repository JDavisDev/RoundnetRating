package com.jdavis.roundnetrating.view

import com.jdavis.roundnetrating.app.Styles
import com.jdavis.roundnetrating.controller.EloController
import com.jdavis.roundnetrating.controller.PlayerController
import javafx.beans.property.SimpleStringProperty
import main.model.Player
import main.model.Team
import tornadofx.*

class MainView : View("Hello TornadoFX") {

    val eloController: EloController by inject()
    val playerController: PlayerController by inject()
    val nameInput = SimpleStringProperty()

    private val teamList = listOf(
            Player(1, "Samantha Stuart", 1500),
            Player(2, "Tom Marks", 1500),
            Player(3, "Stuart Gills", 1500),
            Player(4, "Nicole Williams", 1500)
    ).observable()


    override val root = vbox {
        form {
            fieldset {
                field("New Player: ") {
                    textfield(nameInput)
                }

                button("Add Player") {
                    action {
                        if(nameInput.value != null && nameInput.value.isNotEmpty()) {
                            playerController.addPlayer(nameInput.value)
                        }
                        nameInput.value = ""
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

                }
                field("Player Three") {

                }
                field("Player Four") {

                }





                button("Save Game") {
                    useMaxWidth = true
                    action {

                        nameInput.value = ""
                    }
                }
            }
        }
    }
}