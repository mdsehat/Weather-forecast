package com.example.weatherforecast.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CityEntity::class], version = 1)
abstract class CityDatabase: RoomDatabase() {
    abstract fun dao(): CityDao
}