package com.example.xgoals

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import coil.compose.rememberAsyncImagePainter
import com.example.xgoals.models.match.MatchDetails
import com.example.xgoals.models.match.Shots
import com.example.xgoals.ui.theme.darkGrey
import com.example.xgoals.ui.theme.lightGrey
import com.google.gson.GsonBuilder
import okhttp3.*


class MatchActivity : ComponentActivity() {

    private var matchDetails = mutableStateListOf<MatchDetails>()
    private var shots = mutableStateListOf<Shots>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val matchID: Int = intent.getIntExtra("matchID", 0)

        val url = "https://www.fotmob.com/api/matchDetails?matchId=$matchID"
        val mainHandler = Handler(Looper.getMainLooper())

        mainHandler.post(object : Runnable {
            override fun run() {
                fetchJson(url)
                mainHandler.postDelayed(this, 10000)
            }
        })

        setContent {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(lightGrey),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if(!matchDetails.isEmpty())
                    MatchComposable(matchDetails[0], shots)
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Export Image")
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

                    matchDetails.add(gson.fromJson(body, MatchDetails::class.java))

                    val sortedShots = matchDetails[0].content.shotmap.shots.sortedByDescending { it.expectedGoals }

                    shots.clear()
                    
                    for(shot in sortedShots){
                        shots.add(shot)
                    }
                }
                override fun onFailure(call: Call, e: java.io.IOException) {
                    Log.i("TEST", "Failed to execute request.")
                }
            }
        )
    }
}

@Composable
fun MatchComposable(matchDetails: MatchDetails, shots : List<Shots>){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(darkGrey),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 23.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ){
            Image(
                painter = rememberAsyncImagePainter(model = "https://images.fotmob.com/image_resources/logo/leaguelogo/dark/${matchDetails.general.parentLeagueId}.png"),
                contentDescription = "${matchDetails.general.parentLeagueName} logo",
                modifier = Modifier
                    .size(30.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text =
                    matchDetails.general.homeTeam.name + " " +
                            matchDetails.header.status.scoreStr + " " +
                            matchDetails.general.awayTeam.name,
                    color = Color.White,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Left
                )
                Text(
                    text = matchDetails.general.leagueRoundName + " - " + matchDetails.general.matchTimeUTC,
                    color = Color.White,
                    fontSize = 10.sp,
                    textAlign = TextAlign.Left
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(darkGrey),
            contentAlignment = Alignment.Center
        ){
            Box(
                modifier = Modifier
                    .height(229.dp)
                    .width(341.dp)
                    .background(lightGrey)
            )
            Image(
                painter = painterResource(id = R.drawable.field),
                contentDescription = "field image",
                modifier = Modifier
                    .width(360.dp)
            )
            Canvas(
                modifier = Modifier
                    .height(229.dp)
                    .width(341.dp)
            ) {
                for(shot in shots){
                    // FILL
                    drawCircle(
                        color =  Color(("#80" + shot.teamColor.drop(1)).toColorInt()),
                        radius = (shot.expectedGoals*15).dp.toPx(),
                        center = Offset(
                            x =
                            if(shot.teamId == matchDetails.general.homeTeam.id)
                                ((105-shot.x) / 105 * 341).dp.toPx()
                            else
                                (shot.x / 105 * 341).dp.toPx(),
                            y =
                            if(shot.teamId == matchDetails.general.homeTeam.id)
                                (shot.y / 68 * 229).dp.toPx()
                            else
                                ((68-shot.y) / 68 * 229).dp.toPx()
                        )
                    )

                    // OUTLINE
                    drawCircle(
                        color =
                        if(shot.eventType == "Goal")
                            Color.Red
                        else
                            Color.White,
                        radius = (shot.expectedGoals*15).dp.toPx(),
                        style = Stroke(width = 1.dp.toPx()),
                        center = Offset(
                            x =
                            if(shot.teamId == matchDetails.general.homeTeam.id)
                                ((105-shot.x) / 105 * 341).dp.toPx()
                            else
                                (shot.x / 105 * 341).dp.toPx(),
                            y =
                            if(shot.teamId == matchDetails.general.homeTeam.id)
                                (shot.y / 68 * 229).dp.toPx()
                            else
                                ((68-shot.y) / 68 * 229).dp.toPx()
                        )
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(model = matchDetails.header.teams[0].imageUrl),
                        contentDescription = "${matchDetails.header.teams[0].name} logo",
                        modifier = Modifier
                            .size(40.dp)
                    )
                    Spacer(modifier = Modifier.width(70.dp))
                    Image(
                        painter = rememberAsyncImagePainter(model = matchDetails.header.teams[1].imageUrl),
                        contentDescription = "${matchDetails.header.teams[1].name} logo",
                        modifier = Modifier
                            .size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier
                        .padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(40.dp),
                        contentAlignment = Alignment.Center
                    ){
                        Box(modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(matchDetails.general.teamColors.home.toColorInt()))
                        ) {
                            Text(
                                text = matchDetails.header.teams[0].score.toString(),
                                modifier = Modifier
                                    .padding(horizontal = 8.dp, vertical = 2.dp),
                                color = Color.White,
                                fontSize = 10.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Box(modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(darkGrey)
                    ){
                        Text(
                            text = "Goals",
                            modifier = Modifier
                                .width(70.dp)
                                .padding(vertical = 2.dp),
                            color = Color.White,
                            fontSize = 10.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(40.dp),
                        contentAlignment = Alignment.Center
                    ){
                        Box(modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(matchDetails.general.teamColors.away.toColorInt()))
                        ) {
                            Text(
                                text = matchDetails.header.teams[1].score.toString(),
                                modifier = Modifier
                                    .padding(horizontal = 8.dp, vertical = 2.dp),
                                color = Color.White,
                                fontSize = 10.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(40.dp),
                        contentAlignment = Alignment.Center
                    ){
                        Box(modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(matchDetails.general.teamColors.home.toColorInt()))
                        ) {
                            Text(
                                text = matchDetails.content.stats.stats[0].stats[1].stats[0].toString(),
                                modifier = Modifier
                                    .padding(horizontal = 8.dp, vertical = 2.dp),
                                color = Color.White,
                                fontSize = 10.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Box(modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(darkGrey)
                    ){
                        Text(
                            text = "xGoals",
                            modifier = Modifier
                                .width(70.dp)
                                .padding(vertical = 2.dp),
                            color = Color.White,
                            fontSize = 10.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(40.dp),
                        contentAlignment = Alignment.Center
                    ){
                        Box(modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(matchDetails.general.teamColors.away.toColorInt()))
                        ) {
                            Text(
                                text = matchDetails.content.stats.stats[0].stats[1].stats[1].toString(),
                                modifier = Modifier
                                    .padding(horizontal = 8.dp, vertical = 2.dp),
                                color = Color.White,
                                fontSize = 10.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(40.dp),
                        contentAlignment = Alignment.Center
                    ){
                        Box(modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(matchDetails.general.teamColors.home.toColorInt()))
                        ) {
                            Text(
                                text = matchDetails.content.stats.stats[1].stats[1].stats[0].toString().dropLast(2),
                                modifier = Modifier
                                    .padding(horizontal = 8.dp, vertical = 2.dp),
                                color = Color.White,
                                fontSize = 10.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Box(modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(darkGrey)
                    ){
                        Text(
                            text = "Shots",
                            modifier = Modifier
                                .width(70.dp)
                                .padding(vertical = 2.dp),
                            color = Color.White,
                            fontSize = 10.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(40.dp),
                        contentAlignment = Alignment.Center
                    ){
                        Box(modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(matchDetails.general.teamColors.away.toColorInt()))
                        ) {
                            Text(
                                text = matchDetails.content.stats.stats[1].stats[1].stats[1].toString().dropLast(2),
                                modifier = Modifier
                                    .padding(horizontal = 8.dp, vertical = 2.dp),
                                color = Color.White,
                                fontSize = 10.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(40.dp),
                        contentAlignment = Alignment.Center
                    ){
                        Box(modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(matchDetails.general.teamColors.home.toColorInt()))
                        ) {
                            Text(
                                text = matchDetails.content.stats.stats[1].stats[3].stats[0].toString().dropLast(2),
                                modifier = Modifier
                                    .padding(horizontal = 8.dp, vertical = 2.dp),
                                color = Color.White,
                                fontSize = 10.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Box(modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(darkGrey)
                    ){
                        Text(
                            text = "On Target",
                            modifier = Modifier
                                .width(70.dp)
                                .padding(vertical = 2.dp),
                            color = Color.White,
                            fontSize = 10.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(40.dp),
                        contentAlignment = Alignment.Center
                    ){
                        Box(modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(matchDetails.general.teamColors.away.toColorInt()))
                        ) {
                            Text(
                                text = matchDetails.content.stats.stats[1].stats[3].stats[1].toString().dropLast(2),
                                modifier = Modifier
                                    .padding(horizontal = 8.dp, vertical = 2.dp),
                                color = Color.White,
                                fontSize = 10.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(40.dp),
                        contentAlignment = Alignment.Center
                    ){
                        Box(modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(matchDetails.general.teamColors.home.toColorInt()))
                        ) {
                            Text(
                                text = matchDetails.content.stats.stats[0].stats[0].stats[0].toString().dropLast(2) + "%",
                                modifier = Modifier
                                    .padding(horizontal = 8.dp, vertical = 2.dp),
                                color = Color.White,
                                fontSize = 10.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Box(modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(darkGrey)
                    ){
                        Text(
                            text = "Possession",
                            modifier = Modifier
                                .width(70.dp)
                                .padding(vertical = 2.dp),
                            color = Color.White,
                            fontSize = 10.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(40.dp),
                        contentAlignment = Alignment.Center
                    ){
                        Box(modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(matchDetails.general.teamColors.away.toColorInt()))
                        ) {
                            Text(
                                text = matchDetails.content.stats.stats[0].stats[0].stats[1].toString().dropLast(2) + "%",
                                modifier = Modifier
                                    .padding(horizontal = 8.dp, vertical = 2.dp),
                                color = Color.White,
                                fontSize = 10.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column (
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Outcome:",
                    color = Color.White,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
                Row(
                    modifier = Modifier
                        .padding(vertical = 15.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically) {
                    Canvas(
                        modifier = Modifier
                            .padding(horizontal = (0.5 * 15).dp)
                            .width(0.dp)
                            .height(0.dp)
                    ) {
                        drawCircle(
                            color = Color.White,
                            radius = (0.5 * 15).dp.toPx(),
                            style = Stroke(width = 1.dp.toPx()),
                            center = Offset(
                                x = 0.dp.toPx(),
                                y = 0.dp.toPx()
                            )
                        )
                    }
                    Text(
                        text = "Shot",
                        modifier = Modifier
                            .padding(horizontal = 6.dp),
                        color = Color.White,
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center
                    )
                    Canvas(
                        modifier = Modifier
                            .padding(horizontal = (0.5 * 15).dp)
                            .width(0.dp)
                            .height(0.dp)
                    ) {
                        drawCircle(
                            color = Color.Red,
                            radius = (0.5 * 15).dp.toPx(),
                            style = Stroke(width = 1.dp.toPx()),
                            center = Offset(
                                x = 0.dp.toPx(),
                                y = 0.dp.toPx()
                            )
                        )
                    }
                    Text(
                        text = "Goal",
                        modifier = Modifier
                            .padding(horizontal = 6.dp),
                        color = Color.White,
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Spacer(modifier = Modifier.width(24.dp))
            Column (
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "xG value:",
                    color = Color.White,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
                Row(
                    modifier = Modifier
                        .padding(vertical = 15.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically) {
                    Canvas(
                        modifier = Modifier
                            .padding(horizontal = (0.15 * 15).dp)
                            .width(0.dp)
                            .height(0.dp)
                    ) {
                        drawCircle(
                            color = Color.White,
                            radius = (0.15 * 15).dp.toPx(),
                            style = Stroke(width = 1.dp.toPx()),
                            center = Offset(
                                x = 0.dp.toPx(),
                                y = 0.dp.toPx()
                            )
                        )
                    }
                    Text(
                        text = ".2",
                        modifier = Modifier
                            .padding(horizontal = 6.dp),
                        color = Color.White,
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center
                    )
                    Canvas(
                        modifier = Modifier
                            .padding(horizontal = (0.40 * 15).dp)
                            .width(0.dp)
                            .height(0.dp)
                    ) {
                        drawCircle(
                            color = Color.White,
                            radius = (0.40 * 15).dp.toPx(),
                            style = Stroke(width = 1.dp.toPx()),
                            center = Offset(
                                x = 0.dp.toPx(),
                                y = 0.dp.toPx()
                            )
                        )
                    }
                    Text(
                        text = ".4",
                        modifier = Modifier
                            .padding(horizontal = 6.dp),
                        color = Color.White,
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center
                    )
                    Canvas(
                        modifier = Modifier
                            .padding(horizontal = (0.60 * 15).dp)
                            .width(0.dp)
                            .height(0.dp)
                    ) {
                        drawCircle(
                            color = Color.White,
                            radius = (0.60 * 15).dp.toPx(),
                            style = Stroke(width = 1.dp.toPx()),
                            center = Offset(
                                x = 0.dp.toPx(),
                                y = 0.dp.toPx()
                            )
                        )
                    }
                    Text(
                        text = ".6",
                        modifier = Modifier
                            .padding(horizontal = 6.dp),
                        color = Color.White,
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center
                    )
                    Canvas(
                        modifier = Modifier
                            .padding(horizontal = (0.80 * 15).dp)
                            .width(0.dp)
                            .height(0.dp)
                    ) {
                        drawCircle(
                            color = Color.White,
                            radius = (0.80 * 15).dp.toPx(),
                            style = Stroke(width = 1.dp.toPx()),
                            center = Offset(
                                x = 0.dp.toPx(),
                                y = 0.dp.toPx()
                            )
                        )
                    }
                    Text(
                        text = ".8",
                        modifier = Modifier
                            .padding(horizontal = 6.dp),
                        color = Color.White,
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center
                    )
                    Canvas(
                        modifier = Modifier
                            .padding(horizontal = 15.dp)
                            .width(0.dp)
                            .height(0.dp)
                    ) {
                        drawCircle(
                            color = Color.White,
                            radius = 15.dp.toPx(),
                            style = Stroke(width = 1.dp.toPx()),
                            center = Offset(
                                x = 0.dp.toPx(),
                                y = 0.dp.toPx()
                            )
                        )
                    }
                    Text(
                        text = "1",
                        modifier = Modifier
                            .padding(horizontal = 6.dp),
                        color = Color.White,
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}