package com.example.weatherforecast.view.home

import com.example.weatherforecast.data.model.ForecastResponse

sealed class HomeState{
    object Idle: HomeState()
    object ShowLoading : HomeState()
    data class Error(val message: String): HomeState()
    data class ListOfDays(val list: MutableList<String>): HomeState()

    data class ShowWeatherForecast(val item:ForecastResponse.Hours):HomeState()

    data class ShowGeneralInfoOfCity(val city:String, val sunrise:Int, val sunset:Int, val timezone:Int):HomeState()
}
