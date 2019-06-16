import com.google.gson.annotations.SerializedName

data class MainForecast(
    @SerializedName("temp") var temp: Double,
    @SerializedName("temp_min") val temp_min : Double,
    @SerializedName("temp_max") val temp_max : Double,
    @SerializedName("pressure") val pressure: Double,
    @SerializedName("sea_level") val sea_level : Double,
    @SerializedName("grnd_level") val grnd_level : Double,
    @SerializedName("humidity") var humidity : Int,
    @SerializedName("temp_kf") val temp_kf : Double
)