package com.example.weatherforecast.view.search

import com.example.weatherforecast.data.database.CityEntity

sealed class SearchState{
    object Idle: SearchState()

    data class ShowCities(val cities: List<CityEntity>): SearchState()
}
