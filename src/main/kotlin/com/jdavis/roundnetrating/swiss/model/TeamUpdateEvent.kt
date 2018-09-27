package com.jdavis.roundnetrating.swiss.model

import com.jdavis.roundnetrating.model.Team
import tornadofx.*

class TeamUpdateEvent(val teamList: List<Team>) : FXEvent()