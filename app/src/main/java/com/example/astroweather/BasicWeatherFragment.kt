package com.example.astroweather

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.astroweather.ApiController.WeatherService
import com.example.astroweather.ApiController.key
import WeatherData
import android.widget.ImageView
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BasicWeatherFragment : Fragment() {
    private var cityName: String? = null
    private var temp: Double? = null
    private var pressure: Int? = null
    private var lon: String? = null
    private var lat: String? = null
    private lateinit var tvCityName: TextView
    private lateinit var tvLatLong: TextView
    private lateinit var tvTemperature: TextView
    private lateinit var tvPressure: TextView
    private var weatherData: WeatherData? = null
    private lateinit var pic: ImageView
    private var shouldCheckForUpdate: Boolean = true

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
        val v = inflater.inflate(R.layout.fragment_basic_weather, container, false)
        tvCityName = v.findViewById(R.id.tvCityName)
        tvLatLong = v.findViewById(R.id.tvLatLong)
        tvTemperature = v.findViewById(R.id.tvTemperature)
        tvPressure = v.findViewById(R.id.tvPressure)
        pic = v.findViewById(R.id.imageView)
        updateValues()
        return v
    }

    @SuppressLint("SetTextI18n")
    private fun setTextViews() {
        tvCityName.text = cityName
        tvLatLong.text = "$lat $lon"
        tvTemperature.text = temp.toString() + " C"
        tvPressure.text = pressure.toString() + " hPa"
    }

    fun updateTextViews(cityName: String, lon: String, lat: String, temp: Double?, pressure: Int?) {
        this.cityName = cityName
        this.temp = temp
        this.lon = lon
        this.lat = lat
        this.pressure = pressure
        setTextViews()
    }

    companion object {
        @JvmStatic
        fun newInstance(): BasicWeatherFragment {
            val bwf = BasicWeatherFragment()
            val b = Bundle()
            bwf.arguments = b
            return bwf
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
                    updateTextViews(
                        weatherData!!.name,
                        weatherData?.coord?.lon.toString(),
                        weatherData?.coord?.lat.toString(),
                        weatherData!!.main.temp,
                        weatherData!!.main.pressure
                    )
                    Picasso.get()
                        .load("https://openweathermap.org/img/w/" + weatherData?.weather?.get(0)?.icon + ".png")
                        .into(pic)
                    pic.layoutParams.height = 500
                    pic.layoutParams.width = 500
                    pic.visibility = View.VISIBLE
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
        updateFromNetwork()
        GlobalScope.launch {
            while (shouldCheckForUpdate) {
                if(MainActivity.shouldUpdateBasicWeatherFragment) {
                    updateFromNetwork()
                    MainActivity.shouldUpdateBasicWeatherFragment = false
                }
                delay(1000L)
            }
        }
    }
}
