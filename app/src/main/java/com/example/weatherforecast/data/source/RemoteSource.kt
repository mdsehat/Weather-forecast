package com.example.weatherforecast.data.source

import com.example.weatherforecast.data.server.ApiServices
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RemoteSource @Inject constructor(private val api: ApiServices) {
    fun getCurrent(lat: Double, lon: Double, appId: String) = flow {
        emit(api.getCurrent(lat, lon, appId))
    }

    fun getForecast(lat: Double, lon: Double, appId: String) = flow {
        emit(api.getForecast(lat, lon, appId))
    }

}