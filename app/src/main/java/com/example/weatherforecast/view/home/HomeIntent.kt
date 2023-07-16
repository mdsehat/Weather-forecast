package com.example.weatherforecast.view.home

sealed class HomeIntent{
    object GetListOfDays : HomeIntent()

    data class getCurrentWeather(val lat:Double, val lon:Double, val appId:String): HomeIntent()
    data class GetWeatherForecast(val lat:Double, val lon:Double, val appId:String, val day:Int): HomeIntent()

}
