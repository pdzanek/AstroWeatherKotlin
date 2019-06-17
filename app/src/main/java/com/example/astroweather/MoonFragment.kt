package com.example.astroweather

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class MoonFragment : Fragment() {
    private var moonriseTime: String? = null
    private var moonsetTime: String? = null
    private var nextNewMoon: String? = null
    private var nextFullMoon: String? = null
    private var moonState: String? = null
    private var lunarMonth: String? = null
    private lateinit var tvMoonriseTime: TextView
    private lateinit var tvMoonsetTime: TextView
    private lateinit var tvNextNewMoon: TextView
    private lateinit var tvNextFullMoon: TextView
    private lateinit var tvMoonState: TextView
    private lateinit var tvLunarMonth: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            moonriseTime = it.getString("moonriseTime")
            moonsetTime = it.getString("moonsetTime")
            nextNewMoon = it.getString("nextNewMoon")
            nextFullMoon = it.getString("nextFullMoon")
            moonState = it.getString("moonState")
            lunarMonth = it.getString("lunarMonth")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v =  inflater.inflate(R.layout.fragment_moon, container, false)

        tvMoonriseTime = v.findViewById(R.id.tvMoonriseTime)
        tvMoonsetTime = v.findViewById(R.id.tvMoonsetTime)
        tvNextNewMoon = v.findViewById(R.id.tvNextNewMoon)
        tvNextFullMoon = v.findViewById(R.id.tvNextFullMoon)
        tvMoonState = v.findViewById(R.id.tvMoonState)
        tvLunarMonth = v.findViewById(R.id.tvLunarMonth)
        if ((moonriseTime != null) and (moonsetTime != null) && (nextNewMoon != null) and (nextFullMoon != null) && moonState != null && lunarMonth != null) {
            setTextViews()
        }
        return v
    }

    private fun setTextViews() {
        tvMoonriseTime.text = moonriseTime
        tvMoonsetTime.text = moonsetTime
        tvNextNewMoon.text = nextNewMoon
        tvNextFullMoon.text = nextFullMoon
        tvMoonState.text = moonState
        tvLunarMonth.text = lunarMonth
    }

    fun updateTextViews(moonriseTime: String,moonsetTime: String,nextNewMoon: String,nextFullMoon: String,moonState: String,lunarMonth: String){
        this.moonriseTime=moonriseTime
        this.moonsetTime=moonsetTime
        this.nextNewMoon=nextNewMoon
        this.nextFullMoon=nextFullMoon
        this.moonState=moonState
        this.lunarMonth=lunarMonth
        setTextViews()
    }

    companion object {
        @JvmStatic
        fun newInstance(moonriseTime: String,
                        moonsetTime: String,
                        nextNewMoon: String,
                        nextFullMoon: String,
                        moonState: String,
                        lunarMonth: String
        ):MoonFragment {

            val mf = MoonFragment()
            val b = Bundle()
            b.putString("moonriseTime", moonriseTime)
            b.putString("moonsetTime", moonsetTime)
            b.putString("nextNewMoon", nextNewMoon)
            b.putString("nextFullMoon", nextFullMoon)
            b.putString("moonState", moonState)
            b.putString("lunarMonth", lunarMonth)
            mf.arguments = b
            return mf
        }
    }
}
