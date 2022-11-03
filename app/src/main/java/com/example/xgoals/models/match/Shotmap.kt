package com.example.xgoals.models.match

data class Shotmap (
    val shots: List<Shots>
)

data class Shots(
    val id: Long,
    val eventType: String,
    val teamId: Int,
    //val playerId: Int,
    //val playerName: String,
    val x: Double,
    val y: Double,
    val min: Int,
    val minAdded: Int,
    //val isBlocked: Boolean,
    //val isOnTarget: Boolean,
    //val blockedX: Double,
    //val blockedY: Double,
    //val goalCrossedY: Double,
    //val goalCrossedZ: Double,
    val expectedGoals: Double,
    //val expectedGoalsOnTarget: Double,
    //val shotType: String,
    //val situation: String,
    //val period: String,
    val isOwnGoal: Boolean,
    //val onGoalShot: OnGoalShot,
    //val firstName: String,
    //val lastName: String,
    val teamColor: String
    )

data class OnGoalShot (
    val x: Double,
    val y: Double,
    val zoomRatio: Any
)
