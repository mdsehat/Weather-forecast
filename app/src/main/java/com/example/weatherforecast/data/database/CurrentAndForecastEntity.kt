package com.example.weatherforecast.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weatherforecast.data.model.CurrentResponse
import com.example.weatherforecast.data.model.ForecastResponse
import com.example.weatherforecast.utils.CURRENT_FORECAST_ENTITY

@Entity(tableName = CURRENT_FORECAST_ENTITY)
data class CurrentAndForecastEntity(
    @PrimaryKey
    var id: Int = 0,
    var current: CurrentResponse ?= null,
    var forecast: ForecastResponse ?= null,
)