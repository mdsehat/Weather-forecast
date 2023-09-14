package com.example.weatherforecast.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.weatherforecast.data.model.CityDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StoredCity @Inject constructor(@ApplicationContext private val context: Context) {
    val appContext = context.applicationContext
    companion object{
        private val Context.datastore : DataStore<Preferences> by preferencesDataStore(DATA_STORE)
        private val lat = doublePreferencesKey(LAT_STORE)
        private val lon = doublePreferencesKey(LON_STORE)
    }

    suspend fun saveData(myLat: Double, myLon: Double){
        appContext.datastore.edit {
            it[lat] = myLat
            it[lon] = myLon
        }
    }

    val getData : Flow<CityDataStore?> = appContext.datastore.data.map {
        it[lat]?.let {newLat->
            it[lon]?.let {newLon->
                CityDataStore(newLat, newLon)
            }
        }
    }
}