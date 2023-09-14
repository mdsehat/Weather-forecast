package com.example.weatherforecast.data.database

import androidx.room.TypeConverter
import com.example.weatherforecast.data.model.CurrentResponse
import com.example.weatherforecast.data.model.ForecastResponse
import com.google.gson.Gson


class AppTypeConverter {

    private val gson = Gson()

    @TypeConverter
    fun currentToJson(current: CurrentResponse): String{
        return gson.toJson(current)
    }

    @TypeConverter
    fun stringToCurrent(data: String):CurrentResponse{
        return gson.fromJson(data,CurrentResponse::class.java)
    }

    @TypeConverter
    fun forecastToJson(forecast: ForecastResponse): String{
        return gson.toJson(forecast)
    }

    @TypeConverter
    fun stringToForecast(data: String):ForecastResponse{
        return gson.fromJson(data,ForecastResponse::class.java)
    }

}