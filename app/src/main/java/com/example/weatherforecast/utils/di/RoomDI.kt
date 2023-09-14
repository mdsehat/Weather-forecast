package com.example.weatherforecast.utils.di

import android.content.Context
import androidx.room.Room
import com.example.weatherforecast.data.database.CacheDatabase
import com.example.weatherforecast.data.database.CurrentAndForecastEntity
import com.example.weatherforecast.data.database.CityDatabase
import com.example.weatherforecast.utils.CITY_DATABASE
import com.example.weatherforecast.utils.CURRENT_FORECAST_DATABASE
import com.example.weatherforecast.utils.CURRENT_FORECAST_ENTITY
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomDI {

    @Provides
    @Singleton
    fun provideCityDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, CityDatabase::class.java, CITY_DATABASE)
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .createFromAsset("database/tb_city.db")
            .build()

    @Provides
    @Singleton
    fun provideCacheDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, CacheDatabase::class.java, CURRENT_FORECAST_ENTITY)
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideCityDao(db: CityDatabase) = db.dao()

    @Provides
    @Singleton
    fun provideCacheDao(db: CacheDatabase) = db.dao()

    @Provides
    @Singleton
    fun provideEntity() = CurrentAndForecastEntity()
}