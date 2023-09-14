package com.example.weatherforecast.data.repository

import android.util.Log
import com.example.weatherforecast.data.source.LocalSource
import com.example.weatherforecast.data.source.RemoteSource
import com.example.weatherforecast.view.home.HomeState
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class HomeRepository @Inject constructor(private val remoteSource: RemoteSource,
private val localSource: LocalSource) {
    val remote = remoteSource
    val local = localSource

    fun getListOfDays() : MutableList<String> {
        val listDays: MutableList<String> = mutableListOf()
        val df = SimpleDateFormat("EEE, d MMM", Locale.getDefault())
        val calenderCurrent = Calendar.getInstance()
        listDays.add(df.format(calenderCurrent.time))
        for (i in 1..2) {
            calenderCurrent.add(Calendar.DATE, 1)
            listDays.add(df.format(calenderCurrent.time))
        }
        Log.e("tagView", "getListOfDays: " + listDays.size )
        return listDays
    }
}