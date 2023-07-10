package com.example.weatherforecast.utils

import android.graphics.drawable.Drawable
import com.example.weatherforecast.R

class IconCode {
    fun convertCodeToIcon(id:Int):Int{
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
}