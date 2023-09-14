package com.example.weatherforecast.view.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.data.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: SearchRepository): ViewModel() {

    init {
        handleIntent()
    }
    //Search
    val intent = Channel<SearchIntent>()
    private val _searchState = MutableStateFlow<SearchState>(SearchState.Idle)
    val searchState : StateFlow<SearchState> get()  = _searchState

    private fun handleIntent() = viewModelScope.launch {
        intent.consumeAsFlow().collect{
            when(it){
                is SearchIntent.Searching->{
                    getDataCities(it.search)
                }
            }
        }
    }

    private fun getDataCities(txt: String)= viewModelScope.launch {
        repository.local.getDataCities(txt).collect{
            _searchState.value = SearchState.ShowCities(it)
        }
    }

}