package com.example.weatherforecast.view.home

import com.example.weatherforecast.data.model.CurrentModel

sealed class HomeState{
    object idle: HomeState()
    object showLoading : HomeState()
    data class showCurrent(val response:CurrentModel): HomeState()
    data class error(val message: String): HomeState()
    data class listOfDays(val list: MutableList<String>): HomeState()
}
