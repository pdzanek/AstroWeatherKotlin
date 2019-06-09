package com.example.astroweather

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class SunFragment : Fragment() {
    private var sunriseTime: String? = null
    private var sunriseAzimuth: String? = null
    private var sunsetTime: String? = null
    private var sunsetAzimuth: String? = null
    private var twilightMorning: String? = null
    private var twilightEvening: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var tvSunriseTime: TextView
    private lateinit var tvSunriseAzimuth: TextView
    private lateinit var tvSunsetTime: TextView
    private lateinit var tvSunsetAzimuth: TextView
    private lateinit var tvTwilightMorning: TextView
    private lateinit var tvTwilightEvening: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            sunriseTime = it.getString("sunriseTime")
            sunriseAzimuth = it.getString("sunriseAzimuth")
            sunsetTime = it.getString("sunsetTime")
            sunsetAzimuth = it.getString("sunsetAzimuth")
            twilightMorning = it.getString("twilightMorning")
            twilightEvening = it.getString("twilightEvening")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v =  inflater.inflate(R.layout.fragment_sun, container, false)

        tvSunriseTime = v.findViewById(R.id.tvSunSunriseTime)
        tvSunriseAzimuth = v.findViewById(R.id.tvSunSunriseAzimuth)
        tvSunsetTime = v.findViewById(R.id.tvSunsetTime)
        tvSunsetAzimuth = v.findViewById(R.id.tvSunsetAzimuth)
        tvTwilightMorning = v.findViewById(R.id.tvSunTwilightMorning)
        tvTwilightEvening = v.findViewById(R.id.tvSunTwilightEvening)
        if(sunriseTime !=null && sunriseAzimuth!=null && sunsetTime!=null && sunsetAzimuth!=null && twilightMorning!=null && twilightEvening!=null) {
            setTextViews()
        }
        return v
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
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
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    private fun setTextViews() {
        tvSunriseTime.text = sunriseTime
        tvSunriseAzimuth.text = sunriseAzimuth
        tvSunsetTime.text = sunsetTime
        tvSunsetAzimuth.text = sunsetAzimuth
        tvTwilightMorning.text = twilightMorning
        tvTwilightEvening.text = twilightEvening
    }
    fun updateTextViews(sunriseTime: String,sunriseAzimuth: String,sunsetTime: String,sunsetAzimuth: String,twilightMorning: String,twilightEvening: String){
        this.sunsetTime=sunriseTime
        this.sunriseAzimuth=sunriseAzimuth
        this.sunsetTime=sunsetTime
        this.sunsetAzimuth=sunsetAzimuth
        this.twilightMorning=twilightMorning
        this.twilightEvening=twilightEvening
        setTextViews()
    }

    companion object {
        @JvmStatic
        fun newInstance(
            sunriseTime: String,
            sunriseAzimuth: String,
            sunsetTime: String,
            sunsetAzimuth: String,
            twilightMorning: String,
            twilightEvening: String
        ): SunFragment {

            val sf = SunFragment()
            val b = Bundle()
            b.putString("sunriseTime", sunriseTime)
            b.putString("sunriseAzimuth", sunriseAzimuth)
            b.putString("sunsetTime", sunsetTime)
            b.putString("sunsetAzimuth", sunsetAzimuth)
            b.putString("twilightMorning", twilightMorning)
            b.putString("twilightEvening", twilightEvening)
            sf.arguments = b
            return sf
        }
    }
}
