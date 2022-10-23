package com.example.xgoals

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import com.example.xgoals.model.League
import com.example.xgoals.view.LeagueItem
import com.example.xgoals.viewModel.LeagueViewModel


class MainActivity : ComponentActivity() {

    val leagueViewModel by viewModels<LeagueViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Surface(color = MaterialTheme.colors.background) {
                LeagueList(leagueList = leagueViewModel.leagueListResponse)
                leagueViewModel.getLeagueList()
            }

        }
    }
}

@Composable
fun LeagueList(leagueList: List<League>){
    LazyColumn{
        itemsIndexed(items = leagueList) {index, item -> 
            LeagueItem(league = item)
        }
    }
}