package com.example.astroweather

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_settings.*
import java.lang.Double.parseDouble
import android.preference.PreferenceManager
import android.util.Log

class Settings : AppCompatActivity(), View.OnClickListener {
    private val PREFS_FILENAME = "cityAdapter"
    private var latitude = 0.0
    private var longitude = 0.0
    private var updateTime = 1
    private var readerLongitude = ""
    private var readerLatitude = ""
    private var cityName="Łódź"
    private lateinit var cityList : ArrayList<String>

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.buttonAddCity->{
                if(editCity.text.toString()==""){}
                else {
                    Log.i("button pressed", "true")
                    cityList.add(editCity.text.toString())
                    val arrayAdapterCity = ArrayAdapter(this, android.R.layout.simple_spinner_item, cityList)
                    spinnerCity.adapter = arrayAdapterCity
                    editCity.setText("")
                }
            }
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
                cityName = spinnerCity.selectedItem.toString()
                val preferences = PreferenceManager.getDefaultSharedPreferences(this)
                val editor = preferences.edit()
                editor.putString("latitude", latitude.toString())
                editor.putString("longitude", longitude.toString())
                editor.putInt("updateTime", updateTime)
                editor.putString("cityName", cityName)
                editor.apply()
                val preferencesAdapter = getSharedPreferences(PREFS_FILENAME,0)
                val editorAdapter = preferencesAdapter?.edit()
                var j=0
                while(j<cityList.size) {
                    Log.i("pos$j", cityList[j])
                    editorAdapter?.putString("cityName$j", cityList[j])
                    j+=1
                }
                editorAdapter?.putInt("cityNameCount", cityList.size)
                editorAdapter?.apply()
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
        cityList = ArrayList()
        readSharedPreferences()
        initComponents()
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
        cityName = preferences.getString("cityName", "Łódź")
        val preferencesAdapter = getSharedPreferences(PREFS_FILENAME,0)
        var cityNameCount=0
        cityNameCount = preferencesAdapter.getInt("cityNameCount", 0)
        var i=0
        Log.i("reading preferences", "XD")
        while (i<cityNameCount){
            val valueCity = preferencesAdapter.getString("cityName$i","Łódź")
            cityList.add(valueCity)
            Toast.makeText(this,"Dodano miasto do ulubionych", Toast.LENGTH_SHORT).show()
            i+=1
        }
    }
    private fun initComponents(){
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.spinnerAdapter))
        val arrayAdapterCity = ArrayAdapter(this,android.R.layout.simple_spinner_item,cityList)
        spinnerTime.adapter = arrayAdapter
        spinnerCity.adapter = arrayAdapterCity
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