package uz.bdm.weatheruz.service

import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication


class MyApp : MultiDexApplication() {
    companion object {
        lateinit var app: MyApp
        var imageBaseUrl = ""
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        MultiDex.install(this)
//        Prefs.init(this)
//        ISTClient.initClient(this)

    }
}