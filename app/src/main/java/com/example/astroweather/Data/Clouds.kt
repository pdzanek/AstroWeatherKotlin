import com.google.gson.annotations.SerializedName

public data class Clouds(
    @SerializedName("all") val all : Int
){
    override fun toString(): String {
        return "$all"
    }
}