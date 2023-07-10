package com.example.weatherforecast.data.server

import com.example.weatherforecast.data.model.CurrentModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {

    @GET("weather")
    suspend fun getCurrent(@Query("lat") lat: Double, @Query("lon") lon: Double,
                   @Query("appid") appId: String): Response<CurrentModel>

}