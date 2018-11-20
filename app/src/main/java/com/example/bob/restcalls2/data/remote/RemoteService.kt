package com.example.headg.restcalls.data.remote

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RemoteService {

    @GET("data/2.5/forecast")
fun getWeatherData(@Query("zip") zip: String, @Query("appid") appid: String):Call<ResponseBody>
}