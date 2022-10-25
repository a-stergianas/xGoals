package com.example.xgoals

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.xgoals.models.match.MatchDetails
import com.example.xgoals.models.match.Shots
import com.example.xgoals.ui.theme.darkGrey
import com.example.xgoals.ui.theme.lightGrey
import com.google.gson.GsonBuilder
import okhttp3.*

class MatchActivity : ComponentActivity() {

    private var shots = mutableStateListOf<Shots>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val matchID: Int = intent.getIntExtra("matchID", 0)

        val url = "https://www.fotmob.com/api/matchDetails?matchId=$matchID"

        fetchJson(url)

        setContent {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(text = "xGoals - Match")
                        },
                        backgroundColor = lightGrey,
                        contentColor = Color.White,
                        actions = {
                            IconButton(onClick = { }) {
                                Icon(
                                    painter = painterResource(
                                        id = R.drawable.ic_twotone_calendar_today_24
                                    ),
                                    contentDescription = "calendar icon"
                                )
                            }
                        }
                    )
                }
            ){
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(darkGrey)
                        .verticalScroll(scrollState),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    var sum=0.0
                    for (shot in shots){
                        Text(text = shot.min.toString() +  " - " + shot.expectedGoals.toString())
                        sum+=shot.expectedGoals
                    }
                    Text(text = sum.toString())
                }
            }
        }
    }

    private fun fetchJson(url : String) {
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(
            object: Callback {
                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string()
                    val gson = GsonBuilder().create()
                    val matchDetails = gson.fromJson(body, MatchDetails::class.java)

                    for(i in 0 until matchDetails.content.shotmap.shots.size){
                        shots.add(matchDetails.content.shotmap.shots[i])
                    }
                }
                override fun onFailure(call: Call, e: java.io.IOException) {
                    Log.i("TEST", "Failed to execute request.")
                }
            }
        )
    }
}