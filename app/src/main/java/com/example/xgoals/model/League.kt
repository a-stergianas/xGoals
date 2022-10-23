package com.example.xgoals.model

data class League(
    val ccode : String,
    val id : Int,
    val primaryId : Int,
    val name : String,
    val matches : List<Match>
)
