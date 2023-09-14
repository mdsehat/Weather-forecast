package com.example.weatherforecast.data.source

import com.example.weatherforecast.data.database.CacheDao
import com.example.weatherforecast.data.database.CurrentAndForecastEntity
import com.example.weatherforecast.data.database.CityDao
import javax.inject.Inject

class LocalSource@Inject constructor(private val cityDao: CityDao, private val cacheDao: CacheDao) {
    suspend fun insertToCache(entity: CurrentAndForecastEntity) = cacheDao.insertToCache(entity)
    fun getDataFromCache() = cacheDao.getDataFromCache()
    fun getDataCities(txt: String) = cityDao.getDataCities(txt)
    fun isEmpty() = cacheDao.isEmpty()
}