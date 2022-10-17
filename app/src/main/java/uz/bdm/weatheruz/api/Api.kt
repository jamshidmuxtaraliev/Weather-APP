package uz.bdm.weatheruz.api

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import uz.bdm.weatheruz.model.CurrentWeatherModel
import uz.bdm.weatheruz.model.MainWeatherModel

interface Api {
    @GET("weather")
    fun getCurrentLocationWeather(
        @Query("lat") lat:String,
        @Query("lon") lon:String,
        @Query("appid") app_id:String,
        @Query("units") units:String = "metric"
    ): Observable<CurrentWeatherModel>

    @GET("forecast")
    fun getForecast5DayWeather(
        @Query("lat") lat:String,
        @Query("lon") lon:String,
        @Query("appid") app_id:String,
        @Query("units") units:String = "metric"
    ): Observable<MainWeatherModel>


    @GET("weather")
    fun getSearchCityWeather(
        @Query("q") search_city:String,
        @Query("appid") app_id:String,
        @Query("units") units:String = "metric"
    ): Observable<CurrentWeatherModel>

    @GET("forecast")
    fun getSearchCity5DayWeather(
        @Query("q") search_city:String,
        @Query("appid") app_id:String,
        @Query("units") units:String = "metric"
    ): Observable<MainWeatherModel>
}