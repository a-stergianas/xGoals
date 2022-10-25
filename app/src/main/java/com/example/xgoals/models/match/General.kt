package com.example.xgoals.models.match

data class General(
    val matchId: String,
    val matchName: String,
    val matchRound: String,
    val teamColors: TeamColors,
    val leagueId: Int,
    val leagueName: String,
    val leagueRoundName: String,
    val parentLeagueId: Int,
    val countryCode: String,
    val parentLeagueName: String,
    val parentLeagueSeason: String,
    val homeTeam: HomeTeam,
    val awayTeam: AwayTeam,
    val matchTimeUTC: String,
    val matchTimeUTCDate: String,
    val started: Boolean,
    val finished: Boolean
)

data class HomeTeam(
    val name: String,
    val id: Int
)

data class AwayTeam(
    val name: String,
    val id: Int
)
