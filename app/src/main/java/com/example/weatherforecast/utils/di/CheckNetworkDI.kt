package com.example.weatherforecast.utils.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import com.example.weatherforecast.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CheckNetworkDI {

    @Provides
    @Singleton
    fun provideManager(@ApplicationContext context: Context) =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Provides
    @Singleton
    fun provideRequestNet() = NetworkRequest.Builder().apply {
        addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        //Android P
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            addCapability(NetworkCapabilities.NET_CAPABILITY_FOREGROUND)
        }
    }.build()
}