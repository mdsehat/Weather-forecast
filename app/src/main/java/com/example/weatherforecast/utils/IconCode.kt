package com.example.weatherforecast.utils

import com.example.weatherforecast.R
import javax.inject.Inject

class IconCode @Inject constructor() {
    fun convertCodeToAnimationIcon(id:Int):Int{
        return when(id){
            in 200..232 -> R.raw.thunderstorm
            in 300..321 -> R.raw.rain
            in 500..531 -> R.raw.rain
            in 600..622 -> R.raw.snow
            in 701..781 -> R.raw.wind
            in 801..804 -> 801
            800-> 800
            else -> {0}
        }
    }
    fun convertCodeToIcon(id:Int):Int{
        return when(id){
            in 200..232 -> R.drawable.thunderstorm
            in 300..321 -> R.drawable.cloud_rain
            in 500..531 -> R.drawable.cloud_rain
            in 600..622 -> R.drawable.snow
            in 701..781 -> R.drawable.wind
            in 801..804 -> 801
            800-> 800
            else -> {0}
        }
    }
}