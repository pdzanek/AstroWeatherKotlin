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
    private val PREFS_FILENAME = "BasicWeatherFragment"
    private var cityName: String? = null
    private var temp: Double? = null
    private var pressure: Int? = null
    private var dt: Int? = null
    private var lon: String? = null
    private var lat: String? = null
    private var icon: String? = null
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
        activity?.runOnUiThread {
            MainActivity.latitude="$lat"
            MainActivity.longitude="$lon"
            tvCityName.text = cityName
            tvLatLong.text = "$lat $lon"
            tvTemperature.text = "%.2f".format(temp) + " C"
            tvPressure.text = pressure.toString() + " hPa"
        }
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
        weatherService.groupList((MainActivity.cityName), "metric", key).enqueue(object : Callback<WeatherData> {
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
                    writeSharedPreferences(weatherData?.weather?.get(0)?.icon, weatherData?.dt)
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
                if(MainActivity.shouldUpdateBasicWeatherFragment) {
                    updateFromNetwork()
                    MainActivity.shouldUpdateBasicWeatherFragment = false
                }
                delay(1000L)
            }
        }
    }
    fun writeSharedPreferences(icon: String?, dt: Int?){
        val preferences = this.activity?.getSharedPreferences(PREFS_FILENAME,0)
        val editor = preferences?.edit()
        editor?.putString("cityName", cityName)
        editor?.putString("lon", lon)
        editor?.putString("lat", lat)
        editor?.putFloat("temp",temp!!.toFloat())
        editor?.putInt("pressure",pressure!!.toInt())
        editor?.putString("icon",icon)
        editor?.putInt("dt",dt!!.toInt())
        editor?.apply()
    }
   private fun updateFromSharedPreferences(){
       val preferences = this.activity?.getSharedPreferences(PREFS_FILENAME,0)
       cityName=preferences!!.getString("cityName", "undefined")
       lon=preferences.getString("lon", "0.0")
       lat=preferences.getString("lat", "0.0")
       temp= preferences.getFloat("temp", 0.0f).toDouble()
       icon=preferences.getString("icon", "01d")
       pressure=preferences.getInt("pressure",0)
       dt=preferences.getInt("dt",0)
       setTextViews()
       if(MainActivity.networkConnection){
           Picasso.get()
               .load("https://openweathermap.org/img/w/$icon.png")
               .into(pic)
           pic.layoutParams.height = 500
           pic.layoutParams.width = 500
           pic.visibility = View.VISIBLE
       }
   }
}
