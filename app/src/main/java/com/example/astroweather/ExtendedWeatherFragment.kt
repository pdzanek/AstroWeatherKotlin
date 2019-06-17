package com.example.astroweather

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.astroweather.ApiController.WeatherService
import com.example.astroweather.ApiController.key
import WeatherData
import android.annotation.SuppressLint
import android.widget.TextView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ExtendedWeatherFragment : Fragment() {
    private val PREFS_FILENAME = "ExtendedWeatherFragment"
    private var dt: Int? = null
    private var weatherData: WeatherData? = null
    private var shouldCheckForUpdate: Boolean = true
    private var cityName: String? = null
    private var windSpeed: String? = null
    private var windDeg: String? = null
    private var visibility: String? = null
    private var clouds: String? = null
    private var humidity: String? = null
    private lateinit var tvCityName: TextView
    private lateinit var tvWindSpeed: TextView
    private lateinit var tvWindDeg: TextView
    private lateinit var tvVisibility: TextView
    private lateinit var tvClouds: TextView
    private lateinit var tvHumidity: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        shouldCheckForUpdate = false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_extended_weather, container, false)
        tvCityName=v.findViewById(R.id.tvCityName)
        tvWindSpeed=v.findViewById(R.id.tvWindSpeed)
        tvWindDeg=v.findViewById(R.id.tvWindDeg)
        tvVisibility=v.findViewById(R.id.tvVisibility)
        tvClouds=v.findViewById(R.id.tvClouds)
        tvHumidity=v.findViewById(R.id.tvHumidity)
        updateValues()
        return v
    }

    @SuppressLint("SetTextI18n")
    private fun setTextViews() {
        tvCityName.text = cityName
        tvWindSpeed.text = "$windSpeed m/s"
        tvWindDeg.text = "$windDeg°"
        tvVisibility.text = "$visibility m"
        tvClouds.text = "$clouds%"
        tvHumidity.text = "$humidity%"
    }

    fun updateTextViews(cityName:String?,windSpeed:String?,windDeg:String?,visibility:String?,clouds:String?,humidity:String?) {
        this.cityName = cityName
        this.windSpeed = windSpeed
        this.windDeg = windDeg
        this.visibility = visibility
        this.clouds = clouds
        this.humidity = humidity
        setTextViews()
    }

    companion object {
        @JvmStatic
        fun newInstance(): ExtendedWeatherFragment {
            val ewf = ExtendedWeatherFragment()
            val b = Bundle()
            ewf.arguments = b
            return ewf
        }
    }

    private fun updateFromNetwork() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val weatherService = retrofit.create(WeatherService::class.java)
        weatherService.groupList(("Łódź"), "metric", key).enqueue(object : Callback<WeatherData> {
            override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                Log.i("Retrofit", "failure")
            }
            override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {
                if (response.code() == 200) {
                    weatherData = response.body()
                    weatherData?.main?.temp = weatherData?.main?.temp!!
                    setUpObject(weatherData!!)

                    updateTextViews(weatherData?.name,weatherData?.wind?.speed.toString(),weatherData?.wind?.deg.toString(),
                        weatherData?.visibility.toString(),weatherData?.clouds.toString(),weatherData?.main?.humidity.toString())

                    writeSharedPreferences(weatherData?.dt)
                }
            }
        })
    }

    private fun setUpObject(weatherobject: WeatherData) {
        WeatherObject.base = weatherobject.base
        WeatherObject.clouds = weatherobject.clouds
        WeatherObject.cod = weatherobject.cod
        WeatherObject.coord = weatherobject.coord
        WeatherObject.dt = weatherobject.dt
        WeatherObject.main = weatherobject.main
        WeatherObject.name = weatherobject.name
        WeatherObject.sys = weatherobject.sys
        WeatherObject.timezone = weatherobject.timezone
        WeatherObject.visibility = weatherobject.visibility
        WeatherObject.weather = weatherobject.weather
        WeatherObject.wind = weatherobject.wind
    }

    private fun updateValues() {
        if(MainActivity.networkConnection) {
            updateFromSharedPreferences()
            if(((System.currentTimeMillis() / 1000L).toInt()>dt!!.toInt()+7200) || MainActivity.firstLaunch) {
                updateFromNetwork()
                Log.i("updated from network", dt.toString())
                Log.i("UTC", (System.currentTimeMillis() / 1000L).toString())
            }
        }
        else{
            updateFromSharedPreferences()
        }
        GlobalScope.launch {
            while (shouldCheckForUpdate) {
                if(MainActivity.shouldUpdateExtendedWeatherFragment) {
                    updateFromNetwork()
                    MainActivity.shouldUpdateExtendedWeatherFragment = false
                }
                delay(1000L)
            }
        }
    }
    fun writeSharedPreferences(dt: Int?){
        val preferences = this.activity?.getSharedPreferences(PREFS_FILENAME,0)
        val editor = preferences?.edit()
        editor?.putInt("dt",dt!!.toInt())
        editor?.putString("cityName", cityName)
        editor?.putString("windSpeed", windSpeed)
        editor?.putString("windDeg",windDeg)
        editor?.putString("visibility",visibility)
        editor?.putString("clouds",clouds)
        editor?.putString("humidity",humidity)
        editor?.apply()
    }

    private fun updateFromSharedPreferences(){
        val preferences = this.activity?.getSharedPreferences(PREFS_FILENAME,0)
        dt= preferences?.getInt("dt",0)
        cityName=preferences!!.getString("cityName", "undefined")
        windSpeed=preferences.getString("windSpeed", "0")
        windDeg= preferences.getString("windDeg", "0")
        visibility=preferences.getString("visibility", "0")
        clouds=preferences.getString("clouds","0")
        humidity=preferences.getString("humidity","0")
        dt=preferences.getInt("dt",0)
        setTextViews()
    }
}
