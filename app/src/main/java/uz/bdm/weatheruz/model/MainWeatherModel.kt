package uz.bdm.weatheruz.model

import java.io.Serializable

data class MainWeatherModel(
    var city: City,
    var cnt: Int,
    var cod: String,
    var list: List<Dayly>,
    var message: Int
):Serializable