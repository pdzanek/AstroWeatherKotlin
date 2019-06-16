import com.google.gson.annotations.SerializedName

data class WeatherData (
    @SerializedName("coord") var coord : Coord,
    @SerializedName("weather") var weather: List<Weather>,
    @SerializedName("base") var base : String,
    @SerializedName("main") var main : Main,
    @SerializedName("visibility") var visibility : Int,
    @SerializedName("wind") var wind : Wind,
    @SerializedName("clouds") var clouds : Clouds,
    @SerializedName("dt") var dt : Int,
    @SerializedName("sys") var sys : Sys,
    @SerializedName("timezone") var timezone : Int,
    @SerializedName("id") var id : Int,
    @SerializedName("name") var name : String,
    @SerializedName("cod") var cod : Int
){
    override fun toString(): String {
        return "WeatherData(coord=$coord, weather=$weather, base='$base', main=$main, visibility=$visibility, wind=$wind,clouds=$clouds, dt=$dt, sys=$sys, timezone=$timezone,id=$id, name='$name', cod=$cod)"
    }
}