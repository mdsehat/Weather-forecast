package com.example.weatherforecast.utils

import android.app.Activity
import android.widget.EditText
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

fun RecyclerView.initRv(myAdapter : RecyclerView.Adapter<*>, myLayoutManager : RecyclerView.LayoutManager){
    this.apply {
        adapter = myAdapter
        layoutManager = myLayoutManager
        setHasFixedSize(true)
    }
}

fun convertTemp(tempK: Double) = tempK.toInt() - 273

fun convertUnixToTime(time: Long, timezone: Int, dataFormat: SimpleDateFormat): String{
    val dateOfCity = (time + timezone) * 1000
    val date = Date(dateOfCity)
    dataFormat.timeZone = TimeZone.getTimeZone("GMT")
    return dataFormat.format(date)
}

fun EditText.showKeyboard(activity: Activity){
    requestFocus()
    post {
        WindowCompat.getInsetsController(activity.window, this).show(WindowInsetsCompat.Type.ime())
    }
}