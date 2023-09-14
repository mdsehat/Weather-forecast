package com.example.weatherforecast.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecast.utils.CURRENT_FORECAST_ENTITY
import com.example.weatherforecast.utils.TB_CITY
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {

    @Query("SELECT * FROM ${TB_CITY} WHERE name LIKE '%' || :txt || '%'")
    fun getDataCities(txt: String): Flow<List<CityEntity>>

}