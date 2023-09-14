package com.example.weatherforecast.data.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weatherforecast.utils.TB_CITY
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = TB_CITY)
data class CityEntity (
    @PrimaryKey
    val id: Int,
    val name: String ?= null,
    val lat: Float ?= null,
    val lon: Float ?= null,
    val countryCode: String ?= null
        ) : Parcelable