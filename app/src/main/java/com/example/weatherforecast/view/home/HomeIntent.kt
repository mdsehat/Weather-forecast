package com.example.weatherforecast.view.home

sealed class HomeIntent{
    data class getCurrent(val lat:Double, val lon:Double,val appId:String): HomeIntent()
    object getListOfDays : HomeIntent()
}
