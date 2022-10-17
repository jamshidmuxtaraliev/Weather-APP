package uz.bdm.weatheruz.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_hourly.*
import uz.bdm.weatheruz.Constants
import uz.bdm.weatheruz.R
import uz.bdm.weatheruz.model.Dayly
import uz.bdm.weatheruz.viewmodel.MainViewModel
import kotlin.math.roundToInt

class HourlyFragment(var item: Dayly) : Fragment() {
//    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hourly, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        temp_main_hourly.text = item.main.temp.roundToInt().toString()+"°"
        title_weather_hourly.text = item.weather[0].main
        Glide.with(this).load(Constants.IMAGE_URL+item.weather[0].icon+".png").into(weather_icon_hourly)

        feels_like_hourly.text = item.main.feels_like.toString()+"°"
        temp_min_max_hourly.text = item.main.temp_min.toString().subSequence(0, 2)
            .toString() + "°/" + item.main.temp_max.toString().subSequence(0, 2)
            .toString() + "°"
        wind_speed_hourly.text = item.wind.speed.toString()+" m/s"
        wind_navigate_hourly.rotation = 180+item.wind.deg.toFloat()
        wind_corner_hourly.text = item.wind.deg.toString()+"°"
        humidity_hourly.text = item.main.humidity.toString()+"%"
        pressure_hourly.text = item.main.pressure.toString()+" mPa"
        visibility_hourly.text = (item.visibility/1000).toString()+" km"
        rainny_hourly.text = (item.pop*100).toInt().toString()+"%"
        cloud_hourly.text = item.clouds.all.toString()+"%"
        snow_hourly.text = item.snow.toString()
    }
}