package com.example.astroweather.ApiController

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import WeatherData
import ForecastData

interface WeatherService{
    @GET("weather?")
    fun groupList(@Query("q") name: String, @Query("units") units: String, @Query("APIkey") key: String): Call<WeatherData>
    @GET("forecast?")
    fun getForecast(@Query("q") name: String,@Query("APIkey") key:String, @Query("cnt") cnt: Int): Call<ForecastData>
}