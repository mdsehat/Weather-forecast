package com.example.weatherforecast.view.home

import com.example.weatherforecast.data.database.CurrentAndForecastEntity
import com.example.weatherforecast.data.model.CurrentResponse
import com.example.weatherforecast.data.model.ForecastResponse

sealed class HomeState {
    object Idle : HomeState()
    object ShowLoading : HomeState()
    data class Error(val message: String) : HomeState()
    data class ShowFromCache(val entity: CurrentAndForecastEntity) : HomeState()

    data class ShowCheckCacheAvailable(val isEmpty: Boolean): HomeState()
    data class ShowCurrentAndForecastWeather(
        val pairInfo: Pair<CurrentResponse,
                ForecastResponse>, val listOfDays: MutableList<String>
    ) : HomeState()
}
