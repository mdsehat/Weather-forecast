package com.example.weatherforecast.data.source

import com.example.weatherforecast.data.server.ApiServices
import javax.inject.Inject

class RemoteSource @Inject constructor(private val api: ApiServices) {
    suspend fun getCurrent(lat:Double, lon: Double, appId: String) = api.getCurrent(lat, lon, appId)
}