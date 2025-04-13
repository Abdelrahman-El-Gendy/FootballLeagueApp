package com.example.footballleagueapp.data.remote

import Root
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface FootballApiService {
    @GET("competitions")
    suspend fun getCompetitions(
        @Header("c20b612f2b2446eb9016001e0ffd4658") apiKey: String
    ): Response<Root>
}