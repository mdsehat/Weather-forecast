package com.example.weatherforecast.view.home

import com.example.weatherforecast.data.model.CurrentResponse
import com.example.weatherforecast.data.model.ForecastResponse

sealed class HomeState{
    object Idle: HomeState()
    object ShowLoading : HomeState()
    data class Error(val message: String): HomeState()
    data class ListOfDays(val list: MutableList<String>): HomeState()

    data class ShowListWeatherForecast(val listForecast: MutableList<ForecastResponse.Hours>):HomeState()

    data class ShowItemWeatherForecast(val itemForecast: ForecastResponse.Hours): HomeState()

    data class ShowCurrentWeather(val itemCurrent:CurrentResponse):HomeState()
}
