package com.example.weatherforecast.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [CurrentAndForecastEntity::class], version = 1)
@TypeConverters(AppTypeConverter::class)
abstract class CacheDatabase: RoomDatabase() {
    abstract fun dao(): CacheDao
}