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

class MyWeatherAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager){
    private var basicWeatherFragment: BasicWeatherFragment? = null
    lateinit var extendedWeatherFragment : ExtendedWeatherFragment
    lateinit var forecastFragment : ForecastFragment

    override fun getItem(position: Int): android.support.v4.app.Fragment? {
            when (position) {
                0 -> {
                    basicWeatherFragment = BasicWeatherFragment.newInstance()
                    return basicWeatherFragment
                }
                1 -> {
                    extendedWeatherFragment = ExtendedWeatherFragment()
                    return extendedWeatherFragment
                }
                2 -> {
                    forecastFragment = ForecastFragment()
                    return forecastFragment
                }
            }
            return null
        }

    override fun getCount(): Int {
        return 3
    }
}