package com.example.astroweather

import com.google.gson.annotations.SerializedName
import Coord
import Weather
import Main
import Wind
import Clouds
import Sys


object WeatherObject{
    @SerializedName("coord")
    var coord: Coord? = null
    @SerializedName("weather")
    var weather: List<Weather>? = null
    @SerializedName("base")
    var base: String? = null
    @SerializedName("main")
    var main: Main? = null
    @SerializedName("visibility")
    var visibility: Int? = null
    @SerializedName("wind")
    var wind: Wind? = null
    @SerializedName("clouds")
    var clouds: Clouds? = null
    @SerializedName("dt")
    var dt: Int? = null
    @SerializedName("sys")
    var sys: Sys? = null
    @SerializedName("timezone")
    var timezone: Int? = null
    @SerializedName("id")
    var id: Int? = null
    @SerializedName("name")
    var name: String? = null
    @SerializedName("cod")
    var cod: Int? = null
}