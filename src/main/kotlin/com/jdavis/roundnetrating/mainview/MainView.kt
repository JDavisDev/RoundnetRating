package com.jdavis.roundnetrating.mainview

import com.jdavis.roundnetrating.elo.view.EloMainView
import com.jdavis.roundnetrating.swiss.view.SwissMainView
import tornadofx.*

class MainView : View("Roundnet Rating") {

    override val root =
            vbox {
                paddingAll = 100
                button {
                    text = "ELO"
                    action {
                        openEloPage()
                    }
                }
                button {
                    text = "Swiss"
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