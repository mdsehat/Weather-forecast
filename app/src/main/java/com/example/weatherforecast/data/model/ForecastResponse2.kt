package com.example.weatherforecast.data.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


data class ForecastResponse2(
    @SerializedName("city")
    val city: City?,
    @SerializedName("cnt")
    val cnt: Int?, // 40
    @SerializedName("cod")
    val cod: String?, // 200
    @SerializedName("list")
    val list: List<Hours?>?,
    @SerializedName("message")
    val message: Int? // 0
)  {

    data class City(
        @SerializedName("coord")
        val coord: Coord?,
        @SerializedName("country")
        val country: String?, // IT
        @SerializedName("id")
        val id: Int?, // 3163858
        @SerializedName("name")
        val name: String?, // Zocca
        @SerializedName("population")
        val population: Int?, // 4593
        @SerializedName("sunrise")
        val sunrise: Int?, // 1689133369
        @SerializedName("sunset")
        val sunset: Int?, // 1689188399
        @SerializedName("timezone")
        val timezone: Int? // 7200
    ) {
        data class Coord(
            @SerializedName("lat")
            val lat: Double?, // 44.34
            @SerializedName("lon")
            val lon: Double? // 10.99
        )
    }

    data class Hours(
        @SerializedName("clouds")
        val clouds: Clouds?,
        @SerializedName("dt")
        val dt: Int?, // 1689152400
        @SerializedName("dt_txt")
        val dtTxt: String?, // 2023-07-12 09:00:00
        @SerializedName("main")
        val main: Main?,
        @SerializedName("pop")
        val pop: Double?, // 0.06
        @SerializedName("rain")
        val rain: Rain?,
        @SerializedName("sys")
        val sys: Sys?,
        @SerializedName("visibility")
        val visibility: Int?, // 10000
        @SerializedName("weather")
        val weather: List<Weather?>?,
        @SerializedName("wind")
        val wind: Wind?
    ){

        data class Clouds(
            @SerializedName("all")
            val all: Int? // 33
        )

        data class Main(
            @SerializedName("feels_like")
            val feelsLike: Double?, // 298.23
            @SerializedName("grnd_level")
            val grndLevel: Int?, // 934
            @SerializedName("humidity")
            val humidity: Int?, // 61
            @SerializedName("pressure")
            val pressure: Int?, // 1016
            @SerializedName("sea_level")
            val seaLevel: Int?, // 1016
            @SerializedName("temp")
            val temp: Double?, // 298.09
            @SerializedName("temp_kf")
            val tempKf: Double?, // -1.8
            @SerializedName("temp_max")
            val tempMax: Double?, // 299.89
            @SerializedName("temp_min")
            val tempMin: Double? // 298.09
        )

        data class Rain(
            @SerializedName("3h")
            val h: Double? // 0.59
        )

        data class Sys(
            @SerializedName("pod")
            val pod: String? // d
        )

        data class Weather(
            @SerializedName("description")
            val description: String?, // scattered clouds
            @SerializedName("icon")
            val icon: String?, // 03d
            @SerializedName("id")
            val id: Int?, // 802
            @SerializedName("main")
            val main: String? // Clouds
        )
        data class Wind(
            @SerializedName("deg")
            val deg: Int?, // 303
            @SerializedName("gust")
            val gust: Double?, // 2.77
            @SerializedName("speed")
            val speed: Double? // 0.23
        )
    }
}