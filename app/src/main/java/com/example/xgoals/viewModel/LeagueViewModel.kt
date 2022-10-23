package com.example.xgoals.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.xgoals.model.League
import com.example.xgoals.network.ApiService
import kotlinx.coroutines.launch
import java.lang.Exception

class LeagueViewModel: ViewModel() {
    var leagueListResponse: List<League> by mutableStateOf(listOf())
    var errorMessage: String by mutableStateOf("")

    fun getLeagueList(){
        viewModelScope.launch {
            val apiService = ApiService.getInstance()
            try {
                val leagueList = apiService.getLeagues()
                leagueListResponse = leagueList
            }
            catch (e: Exception){
                errorMessage = e.message.toString()
            }
        }
    }
}