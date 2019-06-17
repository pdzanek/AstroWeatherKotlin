package com.example.astroweather

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class MyPagerAdapter600(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
    lateinit var moonFragment : MoonFragment
    lateinit var sunFragment : SunFragment
    private lateinit var sunriseTime: String
    private lateinit var sunriseAzimuth: String
    private lateinit var sunsetTime: String
    private lateinit var sunsetAzimuth: String
    private lateinit var twilightMorning: String
    private lateinit var twilightEvening: String
    private lateinit var moonriseTime: String
    private lateinit var moonsetTime: String
    private lateinit var nextNewMoon: String
    private lateinit var nextFullMoon: String
    private lateinit var moonState: String
    private lateinit var lunarMonth: String
    override fun getItem(position: Int): android.support.v4.app.Fragment? {
        when (position) {
            0 -> {
                sunFragment = SunFragment.newInstance(
                    sunriseTime,
                    sunriseAzimuth,
                    sunsetTime,
                    sunsetAzimuth,
                    twilightMorning,
                    twilightEvening)
                return sunFragment
            }
            1 ->{
                moonFragment = MoonFragment.newInstance(
                    moonriseTime,
                    moonsetTime,
                    nextNewMoon,
                    nextFullMoon,
                    moonState,
                    lunarMonth
                )
                return moonFragment
            }
        }
        return null
    }

    override fun getCount(): Int {
        return 2
    }
    fun setValues(
        sunriseTime: String,
        sunriseAzimuth: String,
        sunsetTime: String,
        sunsetAzimuth: String,
        twilightMorning: String,
        twilightEvening: String,
        moonriseTime: String,
        moonsetTime: String,
        nextNewMoon: String,
        nextFullMoon: String,
        moonState: String,
        lunarMonth: String
    ) {
        this.sunriseTime=sunriseTime
        this.sunriseAzimuth=sunriseAzimuth
        this.sunsetTime=sunsetTime
        this.sunsetAzimuth=sunsetAzimuth
        this.twilightMorning=twilightMorning
        this.twilightEvening=twilightEvening
        this.moonriseTime=moonriseTime
        this.moonsetTime=moonsetTime
        this.nextNewMoon=nextNewMoon
        this.nextFullMoon=nextFullMoon
        this.moonState=moonState
        this.lunarMonth=lunarMonth
    }
}