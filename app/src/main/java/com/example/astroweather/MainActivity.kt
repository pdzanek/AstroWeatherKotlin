package com.example.astroweather

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.preference.PreferenceManager
import android.support.v4.app.FragmentManager
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import kotlinx.coroutines.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import android.support.v4.app.FragmentPagerAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var moonFragment : MoonFragment
    lateinit var sunFragment : SunFragment
    lateinit var basicWeatherFragment : BasicWeatherFragment
    lateinit var extendedWeatherFragment : ExtendedWeatherFragment
    lateinit var forecastFragment : ForecastFragment
    private var updateTime = 1
    private lateinit var textClock: TextView
    private var shouldUpdateThreadRun: Boolean = true
    private var timeSinceLastUpdate = 0
    private lateinit var dateFormat: DateFormat
    private lateinit var cal: Calendar
    private lateinit var dateTime: String
    private lateinit var viewPager: ViewPager
    private lateinit var viewPagerWeather: ViewPager
    private var latitude = "0.0"
    private var longitude = "0.0"
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
    companion object{
        private lateinit var astroInfo : AstroInfo
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
        }
    }

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        textClock=findViewById(R.id.textClock)
        readSharedPreferences()
        tvLatitude.text = latitude
        tvLongitude.text = longitude
        if (savedInstanceState != null) {
            timeSinceLastUpdate = savedInstanceState.getInt("timeSinceLastUpdate")
        }

        viewPager=findViewById(R.id.viewPager)

        if (this.resources.configuration.smallestScreenWidthDp >= 600) {
            viewPager.adapter = MyPagerAdapter600(supportFragmentManager)
            viewPagerWeather = findViewById(R.id.viewPagerWeather)
            viewPagerWeather.adapter=MyWeatherAdapter(supportFragmentManager)
        }
        else{
            viewPager.adapter=MyPagerAdapter(supportFragmentManager)
        }
        dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        cal = Calendar.getInstance()
        dateTime = (dateFormat.format(cal.time))
        astroInfo = AstroInfo(dateTime,latitude,longitude)
        updateAstroValues(latitude, longitude)
    }

    override fun onRestart() {
        super.onRestart()
        readSharedPreferences()
        tvLatitude.text = latitude
        tvLongitude.text = longitude
        updateAstroValues(latitude, longitude)
        setTextViews()
    }
    inner class MyPagerAdapter600(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
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
    }

    inner class MyWeatherAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
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
    }

    inner class MyPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
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
                2 ->{
                    basicWeatherFragment = BasicWeatherFragment()
                    return basicWeatherFragment
                }
                3 ->{
                    extendedWeatherFragment = ExtendedWeatherFragment()
                    return extendedWeatherFragment
                }
                4 ->{
                    forecastFragment = ForecastFragment()
                    return forecastFragment
                }
            }
            return null
        }
        override fun getCount(): Int {
            return 5
        }
    }

    override fun onStart() {
        super.onStart()
        shouldUpdateThreadRun = true
        GlobalScope.launch {
            while (shouldUpdateThreadRun) {
                delay(1000L)
                setTime()
            }
            Log.i("globalScope", "Killed")
        }
    }

    override fun onStop() {
        super.onStop()
        shouldUpdateThreadRun = false
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState!!.putInt("timeSinceLastUpdate", timeSinceLastUpdate)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, Settings::class.java))
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun updateAstroValues(latitude: String, longitude:String){
        cal = Calendar.getInstance()
        dateTime = (dateFormat.format(cal.time))
        astroInfo.updateValues(dateTime, latitude, longitude)
        sunriseTime = astroInfo.sunSunriseTime
        sunriseAzimuth = astroInfo.sunAzimuthRise
        twilightMorning = astroInfo.sunTwilightMorning
        twilightEvening = astroInfo.sunTwilightEvening
        sunsetTime = astroInfo.sunSunsetTime
        sunsetAzimuth = astroInfo.sunAzimuthSunset
        moonriseTime = astroInfo.moonriseTime
        moonsetTime = astroInfo.moonset
        nextNewMoon = astroInfo.nextNewMoon
        nextFullMoon = astroInfo.nextFullMoon
        moonState = astroInfo.moonState
        lunarMonth = astroInfo.lunarMonth
    }

    private fun setTextViews(){
        sunFragment.updateTextViews(sunriseTime,sunriseAzimuth,sunsetTime,sunsetAzimuth,twilightMorning,twilightEvening)
        moonFragment.updateTextViews(moonriseTime,moonsetTime,nextNewMoon,nextFullMoon,moonState,lunarMonth)
    }
    private fun readSharedPreferences(){
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        latitude = preferences.getString("latitude","0.0")
        longitude = preferences.getString("longitude", "0.0")
        updateTime = preferences.getInt("updateTime", 1)
    }
    @SuppressLint("SimpleDateFormat")
    private fun setTime() {
        runOnUiThread(Runnable {
            try {
                timeSinceLastUpdate++
                if (timeSinceLastUpdate >= updateTime * 60) {
                    updateAstroValues(latitude, longitude)
                    setTextViews()
                    Toast.makeText(this,"Zaktualizowano dane astronomiczne.",Toast.LENGTH_SHORT).show()
                    timeSinceLastUpdate=0
                }
                Log.i("TIME SINCE LAST UPDATE",""+timeSinceLastUpdate)
                dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                cal = Calendar.getInstance()
                dateTime = (dateFormat.format(cal.time))
                textClock.text = dateTime.substring(11)
            } catch (ignored: Exception) {
            }
        })
    }
    private fun updateWeatherInfo(){
        //TODO: API
    }
}
