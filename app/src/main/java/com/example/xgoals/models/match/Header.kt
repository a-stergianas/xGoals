package com.example.xgoals.models.match

import com.example.xgoals.models.home.Status

data class Header(
    val teams: List<Teams>,
    val status: Status,
    val events: Events
)
data class Teams(
    val name: String,
    val id: Int,
    val score: Int,
    val imageUrl: String,
    val pageUrl: String,
    val fifaRank: Int
)
data class Events(
    val teams: Teams,
    val status: Status,
    val events: Events
)