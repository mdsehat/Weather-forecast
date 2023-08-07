package com.example.weatherforecast.view.home

sealed class HomeIntent{
    object GetListOfDays : HomeIntent()
    data class GetCurrentAndForecastWeather(val lat:Double, val lon:Double, val appId:String): HomeIntent()

}
