package com.example.weatherforecast.view.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.data.database.CurrentAndForecastEntity
import com.example.weatherforecast.data.model.CurrentResponse
import com.example.weatherforecast.data.model.ForecastResponse
import com.example.weatherforecast.data.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: HomeRepository) : ViewModel() {

    val intent = Channel<HomeIntent>()
    private val _state = MutableStateFlow<HomeState>(HomeState.Idle)
    val state: StateFlow<HomeState> get() = _state

    init {
        handlingIntent()
    }

    private fun handlingIntent() = viewModelScope.launch {
        intent.consumeAsFlow().collect { homeIntent ->
            when (homeIntent) {
                is HomeIntent.GetCurrentAndForecastWeather -> {
                    callCurrentAndForecastWeather(homeIntent.lat, homeIntent.lon, homeIntent.appId)
                }
                is HomeIntent.ReadFromCache ->{
                    readFromCache()
                }
                is HomeIntent.CheckCacheAvailable ->{
                    checkConnection()
                }
            }
        }
    }

    //Current and forecast
    private fun callCurrentAndForecastWeather(lat: Float, lon: Float, appId: String) =
        viewModelScope.launch {
            _state.value = HomeState.ShowLoading
            //Combine 2 api
            try {
                val currentResponse = repository.remote.getCurrent(lat, lon, appId)
                val forecastResponse = repository.remote.getForecast(lat, lon, appId)
                combine(currentResponse, forecastResponse) { current: Response<CurrentResponse>,
                                                             forecast: Response<ForecastResponse> ->
                    return@combine Pair(current,forecast)
                }.catch {
                    _state.value = HomeState.Error(it.message!!)
                }
                    .collect {
                        if (it.first.isSuccessful) {
                            _state.value = HomeState.ShowCurrentAndForecastWeather(
                                Pair(it.first.body()!!, it.second.body()!!),
                                repository.getListOfDays()
                            )
                            //Caching
                            saveCache(it.first.body()!!, it.second.body()!!)
                        } else {
                            _state.value = handlingErrorCode(it.first)
                        }
                    }
            }catch (e: Exception){
                _state.value = HomeState.Error("Error connection")
            }
        }

    //Caching
    private fun saveEntity(entity: CurrentAndForecastEntity) = viewModelScope.launch{
        repository.local.insertToCache(entity)
    }

    private fun readFromCache() = viewModelScope.launch {
        repository.local.getDataFromCache().collect{
            _state.value = HomeState.ShowFromCache(it)
        }
    }
    private fun saveCache(current: CurrentResponse, forecast: ForecastResponse){
        val entity = CurrentAndForecastEntity(0,current, forecast)
        saveEntity(entity)
    }

    private fun checkConnection() = viewModelScope.launch {
        repository.local.isEmpty().collect{
            _state.value = HomeState.ShowCheckCacheAvailable(it)
        }
    }
    //Handling error code
    private fun <T> handlingErrorCode(response: Response<T>): HomeState {
        return when (response.code()) {
            401 -> {
                HomeState.Error("Invalid your API key.")
            }
            404 -> {
                HomeState.Error("You are searching wrong city.")
            }
            429 -> {
                HomeState.Error("You called more than 60 API in per minute")
            }
            else -> {
                HomeState.Error(response.message())
            }
        }
    }
}

