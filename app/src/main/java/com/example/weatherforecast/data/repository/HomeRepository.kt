package com.example.weatherforecast.data.repository

import com.example.weatherforecast.data.source.RemoteSource
import javax.inject.Inject

class HomeRepository @Inject constructor(private val remoteSource: RemoteSource) {
    val remote = remoteSource
}