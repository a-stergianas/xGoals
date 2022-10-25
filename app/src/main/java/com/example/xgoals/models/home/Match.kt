package com.example.xgoals.models.home

data class Match(
    val id : Int,
    //val leagueId : Int,
    val time : String,
    val home : Team,
    val away : Team,
    val eliminatedTeamId : Int,
    //val statusId : Int,
    //val tournamentStage : String,
    val status : Status,
    //val timeTS : Long,
    //val tv : List<Tv>
)

data class Status(
    val started : Boolean,
    val cancelled : Boolean,
    val finished : Boolean,
    val ongoing : Boolean,
    val startTimeStr : String,
    //val startDateStr : String,
    //val startDateStrShort : String,
    val scoreStr : String,
    val liveTime : LiveTime,
    val reason : Reason,
    //val whoLostOnPenalties: Int,
    //val whoLostOnAggregated: String
)

data class LiveTime(
    val short : String,
    val long : String
)

data class Reason(
    val short : String,
    val long : String
)
/*
data class Tv(
    val name : String,
    val affiliates : List<String>
)
 */
