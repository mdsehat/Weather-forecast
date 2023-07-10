package com.example.weatherforecast.utils.di

import com.example.weatherforecast.data.server.ApiServices
import com.example.weatherforecast.utils.BASE_URL
import com.example.weatherforecast.utils.TIMEOUT
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitDI {

    @Provides
    @Singleton
    fun provideBaseUrl() = BASE_URL

    @Provides
    @Singleton
    fun provideTimeout() = TIMEOUT

    @Provides
    @Singleton
    fun provideClient(time: Long) = OkHttpClient.Builder()
        .connectTimeout(time,TimeUnit.SECONDS)
        .writeTimeout(time,TimeUnit.SECONDS)
        .readTimeout(time,TimeUnit.SECONDS)
        .build()


    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String, client: OkHttpClient): ApiServices =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServices::class.java)
}