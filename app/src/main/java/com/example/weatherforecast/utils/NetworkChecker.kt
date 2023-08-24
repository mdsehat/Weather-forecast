package com.example.weatherforecast.utils

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class NetworkChecker @Inject constructor(private val manager: ConnectivityManager,
private val request: NetworkRequest): ConnectivityManager.NetworkCallback() {

    private val _isNetworkAvailable = MutableStateFlow(false)

    fun checkingNetwork() : MutableStateFlow<Boolean>{
        //Register
        manager.registerNetworkCallback(request, this)

        //Check active net
        val activeNet = manager.activeNetwork
        if (activeNet == null) {
            _isNetworkAvailable.value = false
            return _isNetworkAvailable
        }
        //Capabilities
        val capabilities = manager.getNetworkCapabilities(activeNet)
        if (capabilities == null){
            _isNetworkAvailable.value = false
            return _isNetworkAvailable
        }

        //Check wifi and cellular
        _isNetworkAvailable.value = when{
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)->true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)->true
            else->false
        }
        return _isNetworkAvailable
    }

    override fun onAvailable(network: Network) {
        _isNetworkAvailable.value = true
    }

    override fun onLost(network: Network) {
        _isNetworkAvailable.value = false
    }
}