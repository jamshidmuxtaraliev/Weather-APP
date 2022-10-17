package uz.bdm.weatheruz.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Rain(
    @SerializedName("1h")
    var oneRain: Double?,
    @SerializedName("3h")
    var `threeRain`: Double?
): Serializable