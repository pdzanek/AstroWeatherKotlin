import com.google.gson.annotations.SerializedName

public data class Coord(
    @SerializedName("lon") val lon : Double,
    @SerializedName("lat") val lat : Double
){
    override fun toString(): String{
        return "Coord(lon=$lon, lat=$lat)"
    }
}

