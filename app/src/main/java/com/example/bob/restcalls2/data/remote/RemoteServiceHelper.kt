package com.example.headg.restcalls.data.remote

import android.provider.SyncStateContract
import com.example.bob.restcalls2.KEY.APPID
import com.example.bob.restcalls2.KEY.ZIP
import com.example.bob.restcalls2.WEATHER_BASE_URL
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit

object RemoteServiceHelper {
    var okHttpClient: OkHttpClient

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        okHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    fun getWeatherData(): Call<ResponseBody>{
        val retrofit = Retrofit.Builder().client(okHttpClient).baseUrl(WEATHER_BASE_URL).build()
        val service = retrofit.create(RemoteService::class.java)
        return service.getWeatherData(ZIP, APPID)
    }
}