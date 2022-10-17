package uz.bdm.weatheruz.api

import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import uz.bdm.weatheruz.Constants
import uz.bdm.weatheruz.service.MyApp

object ApiInterface {
    var retrofit:Retrofit?=null
    var api:Api?=null

    fun ApiCreator():Api{
        if (retrofit==null){

            val client = OkHttpClient().newBuilder()
                .addInterceptor(
                    ChuckerInterceptor.Builder(MyApp.app)
                        .collector(ChuckerCollector(MyApp.app))
                        .maxContentLength(1000000L)
                        .redactHeaders(emptySet())
                        .alwaysReadResponseBody(false)
                        .build()
                )
                .build()

            retrofit=Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .build()
            api= retrofit!!.create(Api::class.java)
        }
        return api!!
    }
}