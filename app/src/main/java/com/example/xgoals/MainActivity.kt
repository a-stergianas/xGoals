package com.example.xgoals

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.xgoals.models.home.HomeFeed
import com.example.xgoals.models.home.League
import com.example.xgoals.ui.theme.darkGrey
import com.example.xgoals.ui.theme.lightGrey
import com.example.xgoals.ui.theme.liveGreen
import com.google.gson.GsonBuilder
import okhttp3.*
import java.util.*

class MainActivity : ComponentActivity() {

    private var leagues = mutableStateListOf<League>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val date = Calendar.getInstance()
        val year = date.get(Calendar.YEAR).toString()
        val month =
            if(date.get(Calendar.MONTH)+1-1<10)
                "0${(date.get(Calendar.MONTH)+1)}"
            else
                (date.get(Calendar.MONTH)+1).toString()
        val day =
            if(date.get(Calendar.DAY_OF_MONTH)<10)
                "0${date.get(Calendar.DAY_OF_MONTH)}"
            else
                date.get(Calendar.DAY_OF_MONTH).toString()

        //val url = "https://www.fotmob.com/api/matches?date=$year$month$day"
        val url = "https://www.fotmob.com/api/matches?date=20221024"

        fetchJson(url)

        val intent = Intent(this, MatchActivity::class.java)

        setContent {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(text = "xGoals")
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
                    Spacer(modifier = Modifier.height(12.dp))
                    for (league in leagues){
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                                .border(1.dp, lightGrey, shape = RoundedCornerShape(16.dp)),
                            elevation = 4.dp,
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Row (
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(lightGrey)
                                        .height(40.dp)
                                        .padding(horizontal = 16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ){
                                    Image(
                                        painter = rememberAsyncImagePainter(
                                            "https://images.fotmob.com/image_resources/logo/teamlogo/${league.ccode.lowercase()}.png"
                                        ),
                                        contentDescription = "${league.ccode} flag",
                                        modifier = Modifier
                                            .size(16.dp)
                                            .padding(0.dp)
                                    )
                                    Text(
                                        text = league.name,
                                        color = Color.White,
                                        modifier = Modifier
                                            .padding(horizontal = 8.dp))
                                }
                                for (match in league.matches){
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(Color.White)
                                            .height(48.dp)
                                            .border(1.dp, lightGrey)
                                            .padding(horizontal = 8.dp)
                                            .clickable {
                                                intent.putExtra("matchID", match.id)
                                                startActivity(intent)
                                            },
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ){
                                        Text(
                                            text = match.home.name,
                                            color = Color.Black,
                                            modifier = Modifier
                                                .padding(horizontal = 2.dp)
                                                .width(112.dp),
                                            textAlign = TextAlign.End
                                        )
                                        Image(
                                            painter = rememberAsyncImagePainter(
                                                "https://images.fotmob.com/image_resources/logo/teamlogo/${match.home.id}_xsmall.png"
                                            ),
                                            contentDescription = "${match.home.name} logo",
                                            modifier = Modifier
                                                .size(32.dp)
                                                .padding(horizontal = 2.dp)
                                        )
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            if(match.status.scoreStr != null){
                                                Text(
                                                    text = match.status.scoreStr,
                                                    color =
                                                    if(match.status.liveTime != null)
                                                        liveGreen
                                                    else
                                                        Color.Black,
                                                    modifier = Modifier
                                                        .padding(horizontal = 4.dp)
                                                        .width(30.dp),
                                                    textAlign = TextAlign.Center
                                                )
                                            }
                                            Text(
                                                text =
                                                if(match.status.liveTime != null)
                                                    match.status.liveTime.short
                                                else if(match.status.reason != null)
                                                    match.status.reason.short
                                                else
                                                    match.status.startTimeStr,
                                                color =
                                                if(match.status.liveTime != null)
                                                    liveGreen
                                                else if(match.status.reason != null)
                                                    Color.Black
                                                else
                                                    Color.DarkGray,
                                                modifier = Modifier
                                                    .width(40.dp),
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                        Image(
                                            painter = rememberAsyncImagePainter(
                                                "https://images.fotmob.com/image_resources/logo/teamlogo/${match.away.id}_xsmall.png"
                                            ),
                                            contentDescription = "${match.away.name} logo",
                                            modifier = Modifier
                                                .size(32.dp)
                                                .padding(horizontal = 2.dp)
                                        )
                                        Text(
                                            text = match.away.name,
                                            color = Color.Black,
                                            modifier = Modifier
                                                .padding(horizontal = 2.dp)
                                                .width(1120.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }

    private fun fetchJson(url : String) {

        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(
            object: Callback{
                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string()
                    val gson = GsonBuilder().create()
                    val homeFeed = gson.fromJson(body, HomeFeed::class.java)

                    for(i in 0 until homeFeed.leagues.size){
                        leagues.add(homeFeed.leagues[i])
                    }
                }
                override fun onFailure(call: Call, e: java.io.IOException) {
                    Log.i("TEST", "Failed to execute request.")
                }
            }
        )
    }
}
