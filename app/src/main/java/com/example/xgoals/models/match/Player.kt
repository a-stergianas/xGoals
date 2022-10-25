package com.example.xgoals.models.match

data class Player (
    val playerId: Long,
    val position: Int,
    val matchesWithRating: Int,
    val lastName: String,
    val fullName: String,
    val stats: Stats
)

data class Stats(
    val goals: Int,
    val goalAssist: Int,
    val ontargetScoringAtt: Int,
    val motm: Int,
    val gamesPlayed: Int,
    val minsPlayed: Int,
    val minsPlayedGoal: Int,
    val expectedGoals: Double,
    val playerRating: Float
)