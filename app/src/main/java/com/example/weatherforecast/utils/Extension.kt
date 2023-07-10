package com.example.weatherforecast.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.initRv(myAdapter : RecyclerView.Adapter<*>, myLayoutManager : RecyclerView.LayoutManager){
    this.apply {
        adapter = myAdapter
        layoutManager = myLayoutManager
        setHasFixedSize(true)
    }
}