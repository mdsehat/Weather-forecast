package com.example.weatherforecast.view.home

sealed class HomeIntent{
    data class GetCurrentAndForecastWeather(val lat:Float, val lon:Float, val appId:String): HomeIntent()
    object ReadFromCache: HomeIntent()
    object CheckCacheAvailable: HomeIntent()
}
