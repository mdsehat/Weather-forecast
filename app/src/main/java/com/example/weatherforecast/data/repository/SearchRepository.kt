package com.example.weatherforecast.data.repository

import com.example.weatherforecast.data.source.LocalSource
import javax.inject.Inject

class SearchRepository @Inject constructor(private val localSource: LocalSource) {
    val local = localSource
}