import com.google.gson.annotations.SerializedName

public data class Sys (
    @SerializedName("type") val type : Int,
    @SerializedName("id") val id : Int,
    @SerializedName("message") val message : Double,
    @SerializedName("country") val country : String,
    @SerializedName("sunrise") val sunrise : Int,
    @SerializedName("sunset") val sunset: Int
){
    override fun toString(): String {
        return "Sys(type=$type, id=$id, message=$message, country=$'country', sunrise=$sunrise, sunset=$sunset)"
    }
}