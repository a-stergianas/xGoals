package com.example.xgoals.models.match

data class MatchFacts (
    val matchId: Int,
    //val highlights: Int,
    //val playerOfTheMatch: List<Player1>,
    //val matchesInRound: Int,
    //val events: Int,
    //val infoBox: Int,
    //val teamForm: Int,
    //val odds: Int,
    //val poll: Int,
    //val topPlayers: Int,
    //val insights: Int,
    //val topScorers: Int,
    //val QAData: List<QAData>
)

data class QAData(
    val question: String,
    val answer: String
)