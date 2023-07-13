package com.example.weatherforecast.data.model


import com.google.gson.annotations.SerializedName

data class HourResponse(
    @SerializedName("clouds")
    val clouds: Clouds?,
    @SerializedName("dt")
    val dt: Int?, // 1689152400
    @SerializedName("dt_txt")
    val dtTxt: String?, // 2023-07-12 09:00:00
    @SerializedName("main")
    val main: Main?,
    @SerializedName("pop")
    val pop: Int?, // 0
    @SerializedName("sys")
    val sys: Sys?,
    @SerializedName("visibility")
    val visibility: Int?, // 10000
    @SerializedName("weather")
    val weather: List<Weather?>?,
    @SerializedName("wind")
    val wind: Wind?
) {
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