package com.example.astroweather

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Spinner
import android.widget.ArrayAdapter



class Settings : AppCompatActivity() {

    lateinit var spinnerTime : Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar!!.hide()
        initComponents()
    }
    fun initComponents(){
        spinnerTime = findViewById<Spinner>(R.id.spinnerTime)
        var array_adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.spinnerAdapter))
        spinnerTime.adapter = array_adapter
    }
}
