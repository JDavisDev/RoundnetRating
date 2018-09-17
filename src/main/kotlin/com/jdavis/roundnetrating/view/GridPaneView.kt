package com.jdavis.roundnetrating.view

import tornadofx.*
import javafx.scene.layout.StackPane

class GridPaneView : View("GridPaneView") {
    // global variables
    val numCol: Int = 8

    // every view has a root component
    override val root = gridpane {
        vgap = 15.0
        padding = insets(15)
        for (i in 1..numCol) {
            add(horizontalStreetPane())
        }
    }

    private fun horizontalStreetPane(): StackPane {
        return stackpane {
            rectangle {
                fill = c("4E9830")
                width = 100.0
                height = 100.0
            }
            rectangle {
                fill = c("919191")
                width = 100.0
                height= 40.0
            }
        }
    }
}
