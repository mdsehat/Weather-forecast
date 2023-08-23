package com.example.weatherforecast.view.search

sealed class SearchIntent{
    data class Searching(val search: String): SearchIntent()
}
