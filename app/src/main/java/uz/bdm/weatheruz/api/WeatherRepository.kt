package uz.bdm.weatheruz.api

import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import uz.bdm.weatheruz.model.CurrentWeatherModel
import uz.bdm.weatheruz.model.MainWeatherModel

class WeatherRepository() {
    val compositDisposable = CompositeDisposable()

    fun getCurrentLocationWeather(
        lat:String,
        lon:String,
        app_id:String,
        error: MutableLiveData<String>,
        progress: MutableLiveData<Boolean>,
        success: MutableLiveData<CurrentWeatherModel>
    ) {
        progress.value = true
        compositDisposable.add(
            ApiInterface.ApiCreator().getCurrentLocationWeather(lat, lon, app_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<CurrentWeatherModel>() {
                    override fun onComplete() {

                    }

                    override fun onNext(t: CurrentWeatherModel) {
                        progress.value = false
                        if (t.cod ==200) {
                            success.value = t
                        } else {
                            error.value = t.cod.toString()
                        }
                    }

                    override fun onError(e: Throwable) {
                        progress.value = false
                        error.value = e.localizedMessage
                    }
                })
        )
    }

    fun getForecast5DayWeather(
        lat:String,
        lon:String,
        app_id:String,
        error: MutableLiveData<String>,
        progress: MutableLiveData<Boolean>,
        success: MutableLiveData<MainWeatherModel>
    ) {
        progress.value = true
        compositDisposable.add(
            ApiInterface.ApiCreator().getForecast5DayWeather(lat, lon, app_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<MainWeatherModel>() {
                    override fun onComplete() {

                    }

                    override fun onNext(t: MainWeatherModel) {
                        progress.value = false
                        if (t.cod.toInt() ==200) {
                            success.value = t
                        } else {
                            error.value = t.cod
                        }
                    }

                    override fun onError(e: Throwable) {
                        progress.value = false
                        error.value = e.localizedMessage
                    }
                })
        )
    }


    fun getSearchCityWeather(
        search_city:String,
        app_id:String,
        error: MutableLiveData<String>,
        progress: MutableLiveData<Boolean>,
        success: MutableLiveData<CurrentWeatherModel>
    ) {
        progress.value = true
        compositDisposable.add(
            ApiInterface.ApiCreator().getSearchCityWeather(search_city, app_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<CurrentWeatherModel>() {
                    override fun onComplete() {

                    }

                    override fun onNext(t: CurrentWeatherModel) {
                        progress.value = false
                        if (t.cod ==200) {
                            success.value = t
                        } else {
                            error.value = t.cod.toString()
                        }
                    }

                    override fun onError(e: Throwable) {
                        progress.value = false
                        error.value = e.localizedMessage
                    }
                })
        )
    }

    fun getSearchCity5DayWeather(
        search_city:String,
        app_id:String,
        error: MutableLiveData<String>,
        progress: MutableLiveData<Boolean>,
        success: MutableLiveData<MainWeatherModel>
    ) {
        progress.value = true
        compositDisposable.add(
            ApiInterface.ApiCreator().getSearchCity5DayWeather(search_city, app_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<MainWeatherModel>() {
                    override fun onComplete() {

                    }

                    override fun onNext(t: MainWeatherModel) {
                        progress.value = false
                        if (t.cod.toInt() ==200) {
                            success.value = t
                        } else {
                            error.value = t.cod
                        }
                    }

                    override fun onError(e: Throwable) {
                        progress.value = false
                        error.value = e.localizedMessage
                    }
                })
        )
    }

}