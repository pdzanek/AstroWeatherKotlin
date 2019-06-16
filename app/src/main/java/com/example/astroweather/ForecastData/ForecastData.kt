
import com.google.gson.annotations.SerializedName

data class ForecastData(
    @SerializedName("cod") var cod: Int,
    @SerializedName("message") var message : Double,
    @SerializedName("cnt") var cnt : Int,
    @SerializedName("list") var list : List<ForecastItem>,
    @SerializedName("city") var city: City
)