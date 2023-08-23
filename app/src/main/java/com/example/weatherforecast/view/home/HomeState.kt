package com.example.weatherforecast.view.home

import com.example.weatherforecast.data.model.CurrentResponse
import com.example.weatherforecast.data.model.ForecastResponse

sealed class HomeState{
    object Idle: HomeState()
    object ShowLoading : HomeState()
    data class Error(val message: String): HomeState()
    data class ListOfDays(val listOfDays: MutableList<String>): HomeState()
    data class ShowCurrentAndForecastWeather(val pairInfo: Pair<CurrentResponse, ForecastResponse>):HomeState()
}
