package com.project.getmeals.network.api

import com.google.gson.JsonObject
import com.project.getmeals.data.response.array.WeatherData
import com.project.getmeals.data.response.array.weather.Weather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ServerAPI {
    @GET("weather?")
    fun getCurrentWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("APPID") APPID: String)
            : Call<WeatherData>
}