package uz.bdm.weatheruz.model

import java.io.Serializable

data class Main(
    var feels_like: Double,
    var grnd_level: Int,
    var humidity: Int,
    var pressure: Int,
    var sea_level: Int,
    var temp: Double,
    var temp_kf: Double,
    var temp_max: Double,
    var temp_min: Double
): Serializable