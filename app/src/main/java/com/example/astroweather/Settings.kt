package com.example.astroweather

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_settings.*
import java.lang.Double.parseDouble
import android.preference.PreferenceManager

class Settings : AppCompatActivity(), View.OnClickListener {
    private var latitude = 0.0
    private var longitude = 0.0
    private var updateTime = 1
    private var readerLongitude = ""
    private var readerLatitude = ""

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.buttonSaveAndExit ->{
                if(editLatitude.text.toString() == "" || editLatitude.text.toString() == ".") {
                    latitude=0.0
                    Toast.makeText(applicationContext,"Nie podano szerokości, wartość domyślna: 0.0",Toast.LENGTH_LONG).show()
                }else{
                    latitude = parseDouble(editLatitude.text.toString())
                }
                if(editLongitude.text.toString() == "" || editLongitude.text.toString() == ".") {
                    longitude=0.0
                    Toast.makeText(applicationContext,"Nie podano szerokości, wartość domyślna: 0.0",Toast.LENGTH_LONG).show()
                }else{
                    longitude = parseDouble(editLongitude.text.toString())
                }
                updateTime = try {
                    Integer.parseInt(spinnerTime.selectedItem.toString().substring(0, 2))
                } catch (e: NumberFormatException) {
                    Integer.parseInt(spinnerTime.selectedItem.toString().substring(0, 1))
                }
                val preferences = PreferenceManager.getDefaultSharedPreferences(this)
                val editor = preferences.edit()
                editor.putString("latitude", latitude.toString())
                editor.putString("longitude", longitude.toString())
                editor.putInt("updateTime", updateTime)
                editor.apply()
                super.onBackPressed()
            }
            R.id.buttonExitWithoutSaving ->{
                super.onBackPressed()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar!!.hide()
        initComponents()
        readSharedPreferences()
        if (savedInstanceState != null) {
            editLatitude.setText(savedInstanceState.getString("editLatitude"))
            editLongitude.setText(savedInstanceState.getString("editLongitude"))
        }
        else{
            setSpinnerSelection(updateTime)
            editLatitude.setText(readerLatitude)
            editLongitude.setText(readerLongitude)
        }
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun readSharedPreferences() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        readerLatitude = preferences.getString("latitude","0.0")
        readerLongitude = preferences.getString("longitude", "0.0")
        updateTime = preferences.getInt("updateTime", 1)
    }
    private fun initComponents(){
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.spinnerAdapter))
        spinnerTime.adapter = arrayAdapter
    }

    public override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putString("editLatitude", editLatitude.text.toString())
        savedInstanceState.putString("editLongitude", editLongitude.text.toString())
        super.onSaveInstanceState(savedInstanceState)
    }

    private fun setSpinnerSelection(updateTime: Int) {
        when (updateTime) {
            1 -> spinnerTime.setSelection(0)
            2 -> spinnerTime.setSelection(1)
            3 -> spinnerTime.setSelection(2)
            5 -> spinnerTime.setSelection(3)
            10 -> spinnerTime.setSelection(4)
            15 -> spinnerTime.setSelection(5)
            30 -> spinnerTime.setSelection(6)
            60 -> spinnerTime.setSelection(7)
            else -> spinnerTime.setSelection(0)
        }
    }
}