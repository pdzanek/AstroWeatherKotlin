package com.example.astroweather

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.astroweather.ApiController.WeatherService
import com.example.astroweather.ApiController.key
import ForecastData
import ForecastItem
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ForecastFragment : Fragment() {
    private val PREFS_FILENAME = "ForecastFragment"
    private var forecastData: ForecastData? = null
    private var shouldCheckForUpdate: Boolean = true
    lateinit var recyclerView: RecyclerView

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
        val v = inflater.inflate(R.layout.fragment_forecast, container, false)
        recyclerView =v.findViewById(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager=layoutManager
        updateValues()
        return v
    }

    private fun updateValues() {
        if (MainActivity.networkConnection) {
            updateFromNetwork()
        }
        else{
            getSerializedObject()
            recyclerView.adapter= context?.let { ForecastAdapter(it, forecastData!!.list) }
        }
        GlobalScope.launch {
            while (shouldCheckForUpdate) {
                if (MainActivity.shouldUpdateForecastFragment) {
                    updateFromNetwork()
                    MainActivity.shouldUpdateForecastFragment = false
                }
                delay(1000L)
            }
        }
    }

    private fun setTextViews() {
    }

    companion object {
        @JvmStatic
        fun newInstance(): ForecastFragment {
            val ff = ForecastFragment()
            val b = Bundle()
            ff.arguments = b
            return ff
        }
    }

    private fun updateFromNetwork() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val weatherService = retrofit.create(WeatherService::class.java)
        weatherService.getForecast(MainActivity.cityName, key, "metric",40).enqueue(object : Callback<ForecastData> {
            override fun onFailure(call: Call<ForecastData>, t: Throwable) {
                Log.i("Retrofit", "failure forecast")
            }

            override fun onResponse(call: Call<ForecastData>, response: Response<ForecastData>) {
                if (response.code() == 200) {
                    forecastData = response.body()
                    setUpObject(forecastData!!)
                    forecastData!!.list = setUpList(forecastData!!.list)
                    recyclerView.adapter= context?.let { ForecastAdapter(it, forecastData!!.list) }
                    serializeObject()
                }
            }
        })
    }

    fun setUpList(list: List<ForecastItem>): List<ForecastItem> {
        var forecastList: List<ForecastItem> = ArrayList()
        var i = 8
        while (i < list.size) {
            forecastList = forecastList + list[i-1]
            i += 8
        }
        forecastList = forecastList + list[list.size-1]
        return forecastList
    }

    fun setUpObject(forecastData: ForecastData) {
        ForecastObject.city = forecastData.city
        ForecastObject.cnt = forecastData.cnt
        ForecastObject.cod = forecastData.cod
        ForecastObject.message = ForecastObject.message
        ForecastObject.list = forecastData.list
    }
    private fun serializeObject(){
        val preferences = this.activity?.getSharedPreferences(PREFS_FILENAME,0)
        val editor = preferences?.edit()
        val gson = Gson()
        val json = gson.toJson(forecastData)
        editor?.putString("SerializableObject", json)
        editor?.apply()
    }
    private fun getSerializedObject(){
        val preferences = this.activity?.getSharedPreferences(PREFS_FILENAME,0)
        val gson = Gson()
        val json = preferences?.getString("SerializableObject", "")
        forecastData = gson.fromJson<ForecastData>(json, ForecastData::class.java)
    }
}