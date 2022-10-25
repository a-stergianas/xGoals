package com.example.xgoals.models.match

data class Lineup (
        //val lineup: Lineup,
        //val bench: bench,
        //val naPlayers: naPlayers,
        //val coaches: coaches,
        val teamRatings: teamRatings,
        val hasFantasy: Boolean,
        val usingEnetpulseLineup: Boolean,
        val usingOptaLineup: Boolean,
        val simpleLineup: Boolean
)

data class teamRatings (
        val home: Ratings,
        val away: Ratings
)

class Ratings (
        val num: Float,
        val bgcolor: String
)