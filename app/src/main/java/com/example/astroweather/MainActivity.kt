package com.example.astroweather

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.preference.PreferenceManager
import android.support.annotation.RequiresApi
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
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import com.example.astroweather.WeatherObject as WeatherObject

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATION")
class MainActivity : AppCompatActivity(), View.OnClickListener {
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
    private var networkConnection: Boolean = false
    private lateinit var astroInfo: AstroInfo
    private lateinit var myPagerAdapter: MyPagerAdapter
    private lateinit var myPagerAdapter600: MyPagerAdapter600
    private lateinit var menuItem: MenuItem
    private lateinit var menu: Menu
    private lateinit var offlineIcon: Drawable
    private lateinit var onlineIcon: Drawable
    companion object{
        private lateinit var myWeatherAdapter: MyWeatherAdapter
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        offlineIcon = resources.getDrawable( R.drawable.offline)
        onlineIcon = resources.getDrawable( R.drawable.online)
        setSupportActionBar(toolbar)
        textClock = findViewById(R.id.textClock)
        readSharedPreferences()
        tvLatitude.text = latitude
        tvLongitude.text = longitude
        if (savedInstanceState != null) {
            timeSinceLastUpdate = savedInstanceState.getInt("timeSinceLastUpdate")
        }
        dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        cal = Calendar.getInstance()
        dateTime = (dateFormat.format(cal.time))
        astroInfo = AstroInfo(dateTime, latitude, longitude)
        updateAstroValues(latitude, longitude)
        viewPager = findViewById(R.id.viewPager)

        if (hasNetworkConnection()) {
            Toast.makeText(this, "Dostęp do internetu! Dane zaktualizowane.", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Brak dostępu do internetu, dane mogą być nieaktualne", Toast.LENGTH_LONG).show()
        }
        if (this.resources.configuration.smallestScreenWidthDp >= 600) {
            myPagerAdapter600 = MyPagerAdapter600(supportFragmentManager)
            myPagerAdapter600.setValues(
                sunriseTime,
                sunriseAzimuth,
                sunsetTime,
                sunsetAzimuth,
                twilightMorning,
                twilightEvening,
                moonriseTime,
                moonsetTime,
                nextNewMoon,
                nextFullMoon,
                moonState,
                lunarMonth
            )
            viewPager.adapter = myPagerAdapter600
            viewPagerWeather = findViewById(R.id.viewPagerWeather)
            myWeatherAdapter = MyWeatherAdapter(supportFragmentManager)
            viewPagerWeather.adapter = myWeatherAdapter
        } else {
            myPagerAdapter = MyPagerAdapter(supportFragmentManager)
            myPagerAdapter.setValues(
                sunriseTime,
                sunriseAzimuth,
                sunsetTime,
                sunsetAzimuth,
                twilightMorning,
                twilightEvening,
                moonriseTime,
                moonsetTime,
                nextNewMoon,
                nextFullMoon,
                moonState,
                lunarMonth
            )
            viewPager.adapter = myPagerAdapter
        }
    }
    override fun onRestart() {
        super.onRestart()
        readSharedPreferences()
        tvLatitude.text = latitude
        tvLongitude.text = longitude
        updateAstroValues(latitude, longitude)
    }

    override fun onStart() {
        super.onStart()
        shouldUpdateThreadRun = true
        GlobalScope.launch {
            while (shouldUpdateThreadRun) {
                delay(1000L)
                setTime()
            }
            Log.i("globalScope", "")
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
        this.menu=menu
        return true
    }

    private fun updateAppBarIcon(isOnline: Boolean){
        menuItem = menu.findItem(R.id.has_connection)
        if(isOnline) {
            menuItem.icon=onlineIcon
        }
        else{
            menuItem.icon=offlineIcon
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, Settings::class.java))
                true
            }
            R.id.has_connection -> {
                if (networkConnection) {
                    Toast.makeText(this, "Masz połączenie z internetem!", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this, "Nie masz połączenia z internetem!", Toast.LENGTH_SHORT).show()
                }
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun updateAstroValues(latitude: String, longitude: String) {
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

    private fun readSharedPreferences() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        latitude = preferences.getString("latitude", "0.0")
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
                    Toast.makeText(this, "Zaktualizowano dane astronomiczne.", Toast.LENGTH_SHORT).show()
                    timeSinceLastUpdate = 0
                }
                updateAppBarIcon(hasNetworkConnection())
                dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                cal = Calendar.getInstance()
                dateTime = (dateFormat.format(cal.time))
                textClock.text = dateTime.substring(11)
            } catch (ignored: Exception) {
            }
        })
    }

    private fun hasNetworkConnection(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        networkConnection =
            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).state == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(
                ConnectivityManager.TYPE_WIFI
            ).state == NetworkInfo.State.CONNECTED
        return networkConnection
    }
}