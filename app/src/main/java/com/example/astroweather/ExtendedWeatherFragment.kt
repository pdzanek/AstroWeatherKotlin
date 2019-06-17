package com.example.astroweather

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class ExtendedWeatherFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_extended_weather, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(): ExtendedWeatherFragment {
            val ewf = ExtendedWeatherFragment()
            val b = Bundle()
            ewf.arguments = b
            return ewf
        }
    }
}
