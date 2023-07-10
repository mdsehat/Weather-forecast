package com.example.weatherforecast.view.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.data.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.List

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: HomeRepository): ViewModel() {

    init {
        handlingIntent()
    }
    val intent = Channel<HomeIntent>()
    private val _state = MutableStateFlow<HomeState>(HomeState.idle)
    val state: StateFlow<HomeState> get() = _state

    private fun handlingIntent() = viewModelScope.launch {
        intent.consumeAsFlow().collect{homeIntent->
            when(homeIntent){
                is HomeIntent.getCurrent ->{callCurrent(homeIntent.lat, homeIntent.lon, homeIntent.appId)}
                is HomeIntent.getListOfDays->{getListOfDays()}
            }
        }
    }
    //Current
    private fun callCurrent(lat:Double, lon:Double,appId:String) = viewModelScope.launch {
        _state.value = HomeState.showLoading
        val response = repository.remote.getCurrent(lat, lon, appId)
        if (response.isSuccessful){
            _state.value = HomeState.showCurrent(response.body()!!)
        }else{
            _state.value = HomeState.error(response.message())
        }
    }
    //List of days
    private fun getListOfDays(){
        val listDays : MutableList<String> = mutableListOf()
        val df = SimpleDateFormat("EEE, d MMM", Locale.getDefault())
        val calenderCurrent = Calendar.getInstance()
        listDays.add(df.format(calenderCurrent.time))
        setupCalender(calenderCurrent, listDays, df,1)
        setupCalender(calenderCurrent, listDays, df,2)
        setupCalender(calenderCurrent, listDays, df,3)
        setupCalender(calenderCurrent, listDays, df,4)
        setupCalender(calenderCurrent, listDays, df,5)
        setupCalender(calenderCurrent, listDays, df,6)
        _state.value = HomeState.listOfDays(listDays)
    }

    private fun setupCalender(calenderCurrent: Calendar, listDays: MutableList<String>,df: SimpleDateFormat, i: Int) {
        calenderCurrent.add(Calendar.DATE, 1)
        listDays.add(df.format(calenderCurrent.time))
    }

}