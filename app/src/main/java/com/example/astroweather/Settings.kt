package com.example.astroweather

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView


class Settings : AppCompatActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.buttonSaveAndExit ->{
                Log.i("onClick","buttonSaveAndExit Pressed")
            }
            R.id.buttonExitWithoutSaving ->{
                Log.i("onClick","buttonExitWithoutSaving Pressed")
            }
        }
    }

    private lateinit var editLatitude : TextView
    private lateinit var editLongitude : TextView
    private lateinit var spinnerTime : Spinner
    private lateinit var buttonSaveAndExit : Button
    private lateinit var buttonExitWithoutSaving : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar!!.hide()
        initComponents()
    }
    private fun initComponents(){
        editLatitude = findViewById(R.id.editLatitude)
        editLongitude = findViewById(R.id.editLongitude)
        spinnerTime = findViewById(R.id.spinnerTime)
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.spinnerAdapter))
        spinnerTime.adapter = arrayAdapter
        buttonSaveAndExit = findViewById(R.id.buttonSaveAndExit)
        buttonExitWithoutSaving = findViewById(R.id.buttonExitWithoutSaving)
    }
}
