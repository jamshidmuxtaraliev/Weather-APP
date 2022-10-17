package uz.bdm.weatheruz.model

import java.io.Serializable

data class CurrentWeatherModel(
    var base: String,
    var clouds: Clouds,
    var cod: Int,
    var coord: Coord,
    var dt: Int,
    var id: Int,
    var main: Main,
    var name: String,
    var sys: SysX,
    var timezone: Int,
    var visibility: Int,
    var weather: List<Weather>,
    var wind: Wind,
    var snow: Snow?,
    var rain: Rain?
): Serializable