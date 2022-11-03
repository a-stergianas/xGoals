package com.example.xgoals.models.match

data class Content (
    val matchFacts: MatchFacts,
    //val liveticker: Liveticker,
    //val buzz: Int,
    val stats: Stats1,
    val shotmap: Shotmap,
    //val lineup: Lineup,
    //val playoff: Boolean,
    //val table: Table,
    //val h2h: H2H
)

data class Liveticker (
    val url: String,
    val teams: List<String>,
    val superLiveUrl: String,
    val showSuperLive: Boolean
)

data class Stats1(
    val stats: List<Stats2>,
    val teamColors: TeamColors
)

data class Stats2(
    val title: String,
    val stats: List<Stats3>
)

data class Stats3(
    val title: String,
    val stats: List<Any>,
    val type: String,
    val highlighted: String
)

data class Table(
    val url: String,
    val teams: List<Int>,
    val tournamentNameForUrl: String,
    val parentLeagueId: Int,
    val parentLeagueName: String
)