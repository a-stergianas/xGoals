package com.example.xgoals.network

import com.example.xgoals.model.League
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiService {
    @GET("api/matches?date=20221023")
    suspend fun getLeagues() : List<League>

    companion object{
        var apiService:ApiService? = null
        fun getInstance() : ApiService {
            if(apiService == null){
                apiService = Retrofit.Builder()
                    .baseUrl("https://www.fotmob.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(ApiService::class.java)
            }
            return apiService!!
        }
    }
}