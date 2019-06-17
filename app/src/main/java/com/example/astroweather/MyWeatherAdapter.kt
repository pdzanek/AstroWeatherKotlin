package com.example.astroweather

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.astroweather.WeatherObject as WeatherObject

class MyWeatherAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager){

    private lateinit var extendedWeatherFragment : ExtendedWeatherFragment
    private lateinit var forecastFragment : ForecastFragment
    private lateinit var basicWeatherFragment: BasicWeatherFragment

    override fun getItem(position: Int): android.support.v4.app.Fragment? {
            when (position) {
                0 -> {
                    basicWeatherFragment = BasicWeatherFragment.newInstance()
                    return basicWeatherFragment
                }
                1 -> {
                    extendedWeatherFragment = ExtendedWeatherFragment.newInstance()
                    return extendedWeatherFragment
                }
                2 -> {
                    forecastFragment = ForecastFragment.newInstance()
                    return forecastFragment
                }
            }
            return null
        }

    override fun getCount(): Int {
        return 3
    }
}