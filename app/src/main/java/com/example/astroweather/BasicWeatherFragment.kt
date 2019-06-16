package com.example.astroweather

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class BasicWeatherFragment : Fragment() {
    private var cityName: String? = null
    private var temp: Double? = null
    private var lon: String? = null
    private var lat: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var tvCityName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            cityName = it.getString("cityName")
            temp = it.getDouble("temp")
            lon = it.getString("lon")
            lat = it.getString("lat")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v =  inflater.inflate(R.layout.fragment_basic_weather, container, false)

        tvCityName = v.findViewById(R.id.tvCityName)
        if(cityName !=null) {
            setTextViews()
        }
        return v
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            //throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
    interface OnFragmentInteractionListener {
    }

    private fun setTextViews() {
        tvCityName.text = cityName
    }
    fun updateTextViews(cityName: String, temp: Double?, lon: String, lat: String){
        this.cityName=cityName
        this.temp = temp
        this.lon = lon
        this.lat = lat
        setTextViews()
    }

    companion object {
        @JvmStatic
        fun newInstance(
        ): BasicWeatherFragment {

            val bwf = BasicWeatherFragment()
            val b = Bundle()
            bwf.arguments = b
            return bwf
        }
    }
}
