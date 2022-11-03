package com.example.xgoals.models.home

data class League(
    val ccode : String,
    //val id : Int,
    val primaryId : Int,
    var name : String,
    val matches : List<Match>
)