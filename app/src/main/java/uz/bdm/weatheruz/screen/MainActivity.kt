package uz.bdm.weatheruz.screen

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.navigation_view.view.*
import uz.bdm.weatheruz.Constants
import uz.bdm.weatheruz.Constants.APP_ID
import uz.bdm.weatheruz.Constants.PERMISSON_REQUEST_ACCES_LOCATION
import uz.bdm.weatheruz.R
import uz.bdm.weatheruz.databinding.ActivityMainBinding
import uz.bdm.weatheruz.model.CurrentWeatherModel
import uz.bdm.weatheruz.model.Dayly
import uz.bdm.weatheruz.model.MainWeatherModel
import uz.bdm.weatheruz.service.Functions
import uz.bdm.weatheruz.view.DaylyAdapterListener
import uz.bdm.weatheruz.view.DaylyWeatherAdapter
import uz.bdm.weatheruz.view.HourlyAdapterListener
import uz.bdm.weatheruz.view.HourlyWeatherAdapter
import uz.bdm.weatheruz.viewmodel.MainViewModel
import java.io.Serializable
import java.util.*
import kotlin.math.roundToInt
import java.lang.System.console





class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
//    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainViewModel

    companion object {
        var daylyList = arrayListOf<Dayly>()
        lateinit var allDaylyList: List<Dayly>
    }

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getCurrentNowLocation()

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        viewModel.error.observe(this) {
            Toast.makeText(this, "$it", Toast.LENGTH_LONG).show()
        }

        viewModel.progress.observe(this) {
            setProgres(it)
        }

        viewModel.currentWeatherData.observe(this) {
            setData(it)
        }

        viewModel.forecastWeatherData.observe(this) {
            setRecyclersData(it)
            allDaylyList = it.list
        }

        binding.gotoHourly.setOnClickListener {
            val intent = Intent(this@MainActivity, AllHourlyActivity::class.java)
            startActivity(intent)
        }

        binding.gotoDayily.setOnClickListener {
            val intent = Intent(this@MainActivity, AllDailyActivity::class.java)
            startActivity(intent)
        }

        binding.btnMenu.setOnClickListener {
            binding.fatherLayout.openDrawer(GravityCompat.START)
        }

        binding.btnSetting.setOnClickListener {
            getCurrentNowLocation()
        }

        binding.navigation.lyShare.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_SUBJECT, "Share to: ")
            intent.putExtra(
                Intent.EXTRA_TEXT,
                "Ob havo bashorati uchun mobil ilovamizni yuklab oling \n va baholang !!! \n Created by JAMSHIDBEK"
            )
            startActivity(intent)
        }

        binding.navigation.lyLog_out.setOnClickListener {
            finish()
        }

        binding.btnSearch.setOnClickListener {
            binding.btnCancelSearch.visibility = View.VISIBLE
            binding.btnSearch.visibility = View.GONE
            binding.edSearchCity.visibility = View.VISIBLE
            binding.imgLocation.visibility = View.GONE
            binding.nameCity.visibility = View.GONE
            binding.pustoy.visibility = View.GONE

            showKeyboard()
        }

        binding.btnCancelSearch.setOnClickListener {
            binding.btnCancelSearch.visibility = View.GONE
            binding.btnSearch.visibility = View.VISIBLE
            binding.edSearchCity.visibility = View.GONE
            binding.imgLocation.visibility = View.VISIBLE
            binding.nameCity.visibility = View.VISIBLE
            binding.pustoy.visibility = View.VISIBLE

            hideKeyboard()
        }

        binding.edSearchCity.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (binding.edSearchCity.text.isEmpty()) {
                    binding.edSearchCity.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_baseline_info_24,
                        0
                    );
                } else {
                    performSearch(binding.edSearchCity.text.toString())
                    binding.edSearchCity.text.clear()

                    binding.btnCancelSearch.visibility = View.GONE
                    binding.btnSearch.visibility = View.VISIBLE
                    binding.edSearchCity.visibility = View.GONE
                    binding.imgLocation.visibility = View.VISIBLE
                    binding.nameCity.visibility = View.VISIBLE
                    binding.pustoy.visibility = View.VISIBLE

                    hideKeyboard()

                    return@OnEditorActionListener true
                }
            }
            false
        })


        binding.edSearchCity.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.edSearchCity.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

    }

    fun performSearch(city_name: String) {
        viewModel.getSearchCityWeather(city_name, APP_ID)
        viewModel.getSearchCity5DayWeather(city_name, APP_ID)
    }

    private fun setRecyclersData(data: MainWeatherModel?) {
        binding.recyclerHourly.adapter =
            HourlyWeatherAdapter(data?.list ?: emptyList(), object : HourlyAdapterListener {
                override fun onClickHourly(item: Any?) {
                    val intent = Intent(this@MainActivity, AllHourlyActivity::class.java)
                    intent.putExtra(Constants.EXTRA_DATA, data?.list as Serializable)
                    intent.putExtra(Constants.EXTRA_DATA_2, item as Serializable)
                    startActivity(intent)
                }
            })


        daylyList.clear()
        daylyList.add(data!!.list[0])
        data.list.forEach { day ->
            if (
                (day.dt_txt.subSequence(8, 10).toString()
                    .toInt() != data.list[0].dt_txt.subSequence(8, 10).toString().toInt())
                && (day.dt_txt.subSequence(11, 13).toString().toInt() == 12)
            ) {
                daylyList.add(day)
            }
        }
        binding.recyclerDayly.adapter =
            DaylyWeatherAdapter(daylyList, object : DaylyAdapterListener {
                override fun onClickDay(item: Any?) {
                    if (item is Dayly) {
                        val intent = Intent(this@MainActivity, AllDailyActivity::class.java)
                        intent.putExtra(Constants.EXTRA_DATA, daylyList as Serializable)
                        intent.putExtra(Constants.EXTRA_DATA_2, item as Serializable)
                        startActivity(intent)
                    }
                }
            })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setData(data: CurrentWeatherModel?) {
        if (data != null) {
            binding.nameCity.text = data.name
            binding.windNavigate.rotation = 180 + data.wind.deg.toFloat()
            binding.tempMain.text = data.main.temp.roundToInt().toString() + "°"
            binding.titleWeather.text = data.weather[0].main
            binding.sunRise.text =
                Functions().timeStampToLocalDate(data.sys.sunset!!.toLong()).subSequence(0, 5)
            binding.sunSet.text =
                Functions().timeStampToLocalDate(data.sys.sunrise!!.toLong()).subSequence(0, 5)
            binding.minMaxTem.text = data.main.temp_min.roundToInt()
                .toString() + "°" + "/" + data.main.temp_max.roundToInt().toString() + "°"
            Glide.with(binding.weatherIcon.context)
                .load(Constants.IMAGE_URL + data.weather[0].icon + ".png")
                .into(binding.weatherIcon)
            binding.humidity.text = data.main.humidity.toString() + "%"
            binding.windSpeed.text = data.wind.speed.toString() + "m/s"
            binding.visibility.text = (data.visibility / 1000).toString() + "km"
            binding.pressure.text = data.main.pressure.toString() + "mhPA"
            binding.rainForecast.text = if (data.rain == null) {
                "0 mm"
            } else "${data.rain.toString()}mm"

            updateUI(data.weather[0].id)
        }
    }

    private fun setProgres(it: Boolean?) {
        if (it == true) {
            binding.pbGPS.visibility = View.GONE
            binding.pbLoading.visibility = View.VISIBLE
            binding.rvMainLayout.visibility = View.GONE
        } else {
            binding.pbGPS.visibility = View.GONE
            binding.pbLoading.visibility = View.GONE
            binding.rvMainLayout.visibility = View.VISIBLE
        }
    }

    private fun fetchCurrentLocationWeather(latitude: String, longitude: String) {
        viewModel.getCurrentLocationWeather(latitude, longitude, APP_ID)
        viewModel.getForecast5DayWeather(latitude, longitude, APP_ID)
    }

    fun updateUI(id: Int) {
        if (id in 200..232) {
            binding.fatherLayout.background =
                ContextCompat.getDrawable(this, R.drawable.thunderstrom)
        } else if (id in 300..321) {
            binding.fatherLayout.background = ContextCompat.getDrawable(this, uz.bdm.weatheruz.R.drawable.drizzle)
        } else if (id in 500..531) {
            binding.fatherLayout.background = ContextCompat.getDrawable(this, R.drawable.rain)
        } else if (id in 600..622) {
            binding.fatherLayout.background = ContextCompat.getDrawable(this, R.drawable.snow)
        } else if (id in 701..781) {
            binding.fatherLayout.background = ContextCompat.getDrawable(this, R.drawable.fog)
        } else if (id == 800) {

            if (viewModel.currentWeatherData.value?.weather?.get(0)?.icon?.subSequence(2,3) == "n" ) {
                binding.fatherLayout.background =
                    ContextCompat.getDrawable(this, R.drawable.night_sky)
            } else  {
                binding.fatherLayout.background = ContextCompat.getDrawable(this, R.drawable.day_sky)
            }
        } else if (id == 801) {
            if (viewModel.currentWeatherData.value?.weather?.get(0)?.icon?.subSequence(2,3) == "n" ) {
                binding.fatherLayout.background =
                    ContextCompat.getDrawable(this, R.drawable.night)
            } else  {
                binding.fatherLayout.background = ContextCompat.getDrawable(this, R.drawable.cloudy_sky_one)
            }
        } else if (id == 802) {
            if (viewModel.currentWeatherData.value?.weather?.get(0)?.icon?.subSequence(2,3) == "n" ) {
                binding.fatherLayout.background =
                    ContextCompat.getDrawable(this, R.drawable.night_cloudy)
            } else  {
                binding.fatherLayout.background = ContextCompat.getDrawable(this, R.drawable.cloudy_sky_two)
            }
        } else if (id in 803..804) {
            binding.fatherLayout.background =
                ContextCompat.getDrawable(this, R.drawable.cloudy_sky_three)
        }
    }

    private fun getCurrentNowLocation() {
        if (checkPermissons()) {
            if (isLocationEnabled()) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissons()
                    return
                }

                var locationRequest = LocationRequest()
                locationRequest.interval = 10000
                locationRequest.fastestInterval = 5000
                locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

                LocationServices.getFusedLocationProviderClient(this)
                    .requestLocationUpdates(locationRequest, object : LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult) {
                            super.onLocationResult(locationResult)
                            LocationServices.getFusedLocationProviderClient(this@MainActivity)
                                .removeLocationUpdates(this)
                            if (locationResult.locations != null) {
                                var indx = locationResult.locations.size - 1
                                fetchCurrentLocationWeather(
                                    locationResult.locations[indx].latitude.toString(),
                                    locationResult.locations[indx].longitude.toString()
                                )
                            }
                        }
                    }, Looper.getMainLooper())

            } else {
                //open settings here
                Toast.makeText(this, "Turon on Location", Toast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivityForResult(intent, 2002)
            }
        } else {
            //request permissons here
            requestPermissons()
        }
    }

    private fun checkPermissons(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun requestPermissons() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), PERMISSON_REQUEST_ACCES_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSON_REQUEST_ACCES_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Granted", Toast.LENGTH_SHORT).show()
                getCurrentNowLocation()
            } else {
                Toast.makeText(this, "Denided", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2002) {
            getCurrentNowLocation()
        }
    }

    fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(ed_search_city.getWindowToken(), 0)
    }

    private fun showKeyboard() {
//        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)

        val editText = binding.edSearchCity
        editText.requestFocus()
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }


}