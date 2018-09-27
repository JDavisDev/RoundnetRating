package com.jdavis.roundnetrating.mainview

import com.jdavis.roundnetrating.elo.view.EloMainView
import com.jdavis.roundnetrating.swiss.view.SwissMainView
import javafx.scene.text.TextAlignment
import tornadofx.*

class MainView : View("Roundnet Rating") {

    override val root =
            hbox {
                paddingAll = 600
//                button {
//                    text = "ELO"
//                    minWidth = 50.0
//                    minHeight = 50.0
//                    paddingRight = 25
//                    action {
//                        openEloPage()
//                    }
//                }
                button {
                    text = "Swiss"
                    textAlignment = TextAlignment.CENTER
                    minWidth = 200.0
                    minHeight = 150.0
                    action {
                        openSwissPage()
                    }
                }
            }

    private fun openEloPage() {
        replaceWith(EloMainView::class)

    }

    private fun openSwissPage() {
        replaceWith(SwissMainView::class)
    }
}