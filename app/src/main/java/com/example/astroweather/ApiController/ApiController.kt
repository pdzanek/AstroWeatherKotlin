package com.example.astroweather.ApiController

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import WeatherData


class ApiController{
    var retrofit = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/data/2.5/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var weatherService = retrofit.create<WeatherService>()

    fun getWeatherData(name:String): WeatherData?{
        val call = weatherService.groupList(name,"metric",key).execute()
        return call.body()
    }
}

