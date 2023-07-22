package com.example.weatherforecast.view.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.data.model.ForecastResponse
import com.example.weatherforecast.data.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
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
                is HomeIntent.GetListOfDays -> { getListOfDays() }
                is HomeIntent.GetWeatherForecast -> { callWeatherForecast(homeIntent.lat, homeIntent.lon, homeIntent.appId) }
                is HomeIntent.GetCurrentWeather->{ callCurrentWeather(homeIntent.lat, homeIntent.lon, homeIntent.appId) }
            }
        }
    }
    //Current
    private fun callCurrentWeather(lat: Double, lon: Double, appId: String) = viewModelScope.launch {
        _state.value = HomeState.ShowLoading
        val response = repository.remote.getCurrent(lat, lon, appId)
        if (response.isSuccessful){
            _state.value = HomeState.ShowCurrentWeather(response.body()!!)
        }else{
            _state.value = handlingErrorCode(response)
        }
    }
    //Forecast
    private fun callWeatherForecast(lat: Double, lon: Double, appId: String) =
        viewModelScope.launch {
            val response = repository.remote.getForecast(lat, lon, appId)
            if (response.isSuccessful){
                _state.value = HomeState.ShoWeatherForecast(response.body()!!)
            }else{
                _state.value = handlingErrorCode(response)
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
    private fun <T>handlingErrorCode(response:Response<T>): HomeState{
        return when(response.code()){
            401->{HomeState.Error("Invalid your API key.")}
            404->{HomeState.Error("You are searching wrong city.")}
            429->{HomeState.Error("You called more than 60 API in per minute")}
            else->{HomeState.Error(response.message())}
        }
    }
}

