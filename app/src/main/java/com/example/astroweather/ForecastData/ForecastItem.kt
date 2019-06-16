import com.google.gson.annotations.SerializedName

public data class ForecastItem (
    @SerializedName("dt") val dt : Int,
    @SerializedName("main") val main : MainForecast,
    @SerializedName("weather") val weather : List<Weather>,
    @SerializedName("clouds") val clouds : Clouds,
    @SerializedName("wind") val wind : Wind,
    @SerializedName("rain") val rain: Rain,
    @SerializedName("sys") val sys : Sys,
    @SerializedName("dt_txt") var dt_txt : String
)