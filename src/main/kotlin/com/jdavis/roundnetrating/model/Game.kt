package main.model

import com.jdavis.roundnetrating.model.Team

data class Game(var id: Int, var teamOne: Team, var scoreOne: Int, var teamTwo: Team, var scoreTwo: Int)