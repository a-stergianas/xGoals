package com.example.xgoals.model

data class Match(
    val id : Int,
    val leagueId : Int,
    val time : String,
    val home : Team,
    val away : Team,
    val eliminatedTeamId : Int,
    val statusId : Int,
    val tournamentStage : String,
    val status : Status,
    val timeTS : Int,
    val tv : Tv,
)
