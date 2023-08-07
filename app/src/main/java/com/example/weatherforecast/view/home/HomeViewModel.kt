package com.example.weatherforecast.view.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.data.model.CurrentResponse
import com.example.weatherforecast.data.model.ForecastResponse
import com.example.weatherforecast.data.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: HomeRepository) : ViewModel() {

    init {
        handlingIntent()
    }

    val intent = Channel<HomeIntent>()
    private val _state = MutableStateFlow<HomeState>(HomeState.Idle)
    val state: StateFlow<HomeState> get() = _state

    private fun handlingIntent() = viewModelScope.launch {
        intent.consumeAsFlow().collect { homeIntent ->
            when (homeIntent) {
                is HomeIntent.GetListOfDays -> {
                    getListOfDays()
                }
                is HomeIntent.GetCurrentAndForecastWeather -> {
                    callCurrentAndForecastWeather(homeIntent.lat, homeIntent.lon, homeIntent.appId)
                }
            }
        }
    }

    //Current
    private fun callCurrentAndForecastWeather(lat: Double, lon: Double, appId: String) =
        viewModelScope.launch {
            Log.i("tagview", "callCurrentAndForecastWeather: ")
            _state.value = HomeState.ShowLoading
            //Combine 2 api
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
                            Pair(it.first.body()!!, it.second.body()!!)
                        )
                    } else {
                        _state.value = handlingErrorCode(it.first)
                    }
                }
        }

    //List of days
    private fun getListOfDays() {
        val listDays: MutableList<String> = mutableListOf()
        val df = SimpleDateFormat("EEE, d MMM", Locale.getDefault())
        val calenderCurrent = Calendar.getInstance()
        listDays.add(df.format(calenderCurrent.time))
        for (i in 1..4) {
            calenderCurrent.add(Calendar.DATE, 1)
            listDays.add(df.format(calenderCurrent.time))
        }
        _state.value = HomeState.ListOfDays(listDays)
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

