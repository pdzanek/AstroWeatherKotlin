package com.example.astroweather

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.util.Log
import com.example.astroweather.ApiController.WeatherService
import com.example.astroweather.ApiController.key
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import WeatherData
import com.example.astroweather.WeatherObject as WeatherObject

class MyWeatherAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
    lateinit var basicWeatherFragment : BasicWeatherFragment
    lateinit var extendedWeatherFragment : ExtendedWeatherFragment
    lateinit var forecastFragment : ForecastFragment
    private lateinit var cityName: String
    private var temp: Double? = null
    lateinit var lon: String
    private lateinit var lat: String
    private var pressure: Int? = null
    private var weatherData: WeatherData? = null

    override fun getItem(position: Int): android.support.v4.app.Fragment? {
        when (position) {
            0 ->{
                basicWeatherFragment = BasicWeatherFragment()
                return basicWeatherFragment
            }
            1 ->{
                extendedWeatherFragment = ExtendedWeatherFragment()
                return extendedWeatherFragment
            }
            2 ->{
                forecastFragment = ForecastFragment()
                return forecastFragment
            }
        }
        return null
    }

    override fun getCount(): Int {
        return 3
    }

    fun setTextViews() {
        basicWeatherFragment.updateTextViews(cityName,temp,lon,lat)
    }

    fun setValues(cityName: String, lon: String, lat: String, temp: Double, pressure: Int){
        this.cityName=cityName
        this.lon=lon
        this.lat=lat
        this.temp=temp
        this.pressure=pressure
    }

     fun updateFromNetwork(){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val weatherService = retrofit.create(WeatherService::class.java)
        var call = weatherService.groupList(("lodz"),"metric", key).enqueue(object: Callback<WeatherData> {
            override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                Log.i("Retrofit", "failure")
            }
            override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {
                if(response.code()==200){
                    weatherData = response.body()
                    weatherData?.main?.temp=weatherData?.main?.temp!!
                    setUpObject(weatherData!!)
                    Log.i("City",weatherData!!.name)
                    Log.i("lon",weatherData?.coord?.lon.toString())
                    Log.i("lat",weatherData?.coord?.lat.toString())
                    Log.i("Temp",weatherData!!.main.temp.toString())
                    setValues(weatherData!!.name,weatherData?.coord?.lon.toString(),weatherData?.coord?.lat.toString(), weatherData!!.main.temp,
                        weatherData!!.main.pressure)
                }
            }
        })
    }
    private fun setUpObject(weatherobject: WeatherData){
        WeatherObject.base=weatherobject.base
        WeatherObject.clouds=weatherobject.clouds
        WeatherObject.cod=weatherobject.cod
        WeatherObject.coord=weatherobject.coord
        WeatherObject.dt=weatherobject.dt
        WeatherObject.main=weatherobject.main
        WeatherObject.name=weatherobject.name
        WeatherObject.sys=weatherobject.sys
        WeatherObject.timezone=weatherobject.timezone
        WeatherObject.visibility=weatherobject.visibility
        WeatherObject.weather=weatherobject.weather
        WeatherObject.wind=weatherobject.wind
    }
}