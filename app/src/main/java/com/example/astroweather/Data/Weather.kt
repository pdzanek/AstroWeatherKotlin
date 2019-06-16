import com.google.gson.annotations.SerializedName

public data class Weather(
    @SerializedName("id") val id : Int,
    @SerializedName("main") val main : String,
    @SerializedName("description") val description : String,
    @SerializedName("icon") val icon: String
){
    override fun toString(): String{
        return "Weather(id=$id, main='$main', desription='$description',icon='$icon')"
    }
}