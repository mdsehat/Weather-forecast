package com.example.weatherforecast.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecast.utils.CURRENT_FORECAST_ENTITY
import com.example.weatherforecast.utils.TB_CITY
import kotlinx.coroutines.flow.Flow

@Dao
interface CacheDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToCache(entity: CurrentAndForecastEntity)

    @Query("SELECT * FROM ${CURRENT_FORECAST_ENTITY}")
    fun getDataFromCache(): Flow<CurrentAndForecastEntity>


    @Query("SELECT (SELECT COUNT(*) FROM ${CURRENT_FORECAST_ENTITY}) == 0")
    fun isEmpty() : Flow<Boolean>

}