package uz.bdm.weatheruz.model

import java.io.Serializable

data class SysX(
    var country: String?,
    var id: Int?,
    var sunrise: Int?,
    var sunset: Int?,
    var type: Int?,
    var pod: String?
): Serializable