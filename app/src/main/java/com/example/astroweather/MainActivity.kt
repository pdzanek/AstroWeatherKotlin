package com.example.astroweather

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.FragmentManager
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import android.support.v4.app.FragmentPagerAdapter

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

    override fun onClick(v: View?) {
        when (v!!.id) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        if (savedInstanceState != null) {
            timeSinceLastUpdate = savedInstanceState.getInt("timeSinceLastUpdate")
        }
        viewPager = findViewById(R.id.viewPager)
        viewPager.adapter = MyPagerAdapter(supportFragmentManager)
        initComponents()
    }

    inner class MyPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

        override fun getItem(position: Int): android.support.v4.app.Fragment? {
            when (position) {
                0 -> {
                    sunFragment = SunFragment()
                    return sunFragment
                }
                1 ->{
                    moonFragment = MoonFragment()
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

    @SuppressLint("SimpleDateFormat")
    private fun setTime() {
        runOnUiThread(Runnable {
            try {
                timeSinceLastUpdate++
                if (timeSinceLastUpdate >= updateTime * 60) {

                }
                dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                cal = Calendar.getInstance()
                dateTime = (dateFormat.format(cal.time))
                textClock.text = dateTime.substring(11)
            } catch (ignored: Exception) {
            }
        })
    }

    private fun initComponents() {
        textClock = findViewById(R.id.textClock)
    }
}
