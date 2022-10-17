package uz.bdm.weatheruz.model

import java.io.Serializable

data class Dayly(
    var clouds: Clouds,
    var dt: Int,
    var dt_txt: String,
    var main: Main,
    var pop: Double,
    var rain:Rain?,
    var snow:Snow?,
    var sys: SysX,
    var visibility: Int,
    var weather: List<Weather>,
    var wind: Wind
):Serializable
