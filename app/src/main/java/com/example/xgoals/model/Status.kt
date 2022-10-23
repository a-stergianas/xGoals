package com.example.xgoals.model

data class Status(
    val started : Boolean,
    val cancelled : Boolean,
    val finished : Boolean,
    val startTimeStr : String,
    val startDateStr : String,
    val startDateStrShort : String
)
