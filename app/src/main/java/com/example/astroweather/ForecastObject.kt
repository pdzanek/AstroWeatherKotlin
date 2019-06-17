package com.example.astroweather

import com.google.gson.annotations.SerializedName
import ForecastItem
import City

object ForecastObject {
    @SerializedName("cod")
    var cod: Int? = null
    @SerializedName("message")
    var message: Double? = null
    @SerializedName("cnt")
    var cnt: Int? = null
    @SerializedName("list")
    var list: List<ForecastItem>? = null
    @SerializedName("city")
    var city: City? = null
}