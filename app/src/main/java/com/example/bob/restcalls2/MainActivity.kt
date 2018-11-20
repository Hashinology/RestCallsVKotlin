package com.example.bob.restcalls2

import android.app.Person
import android.content.Intent
import android.content.IntentFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.headg.restcalls.data.remote.RemoteServiceHelper
import com.example.headg.restcalls.models.WeatherData
import com.google.gson.Gson
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private lateinit var nativeReciever: NativeReciever

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        nativeReciever = NativeReciever()
    }

    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter()
        intentFilter.addAction(NATIVE_RECIEVER_ACTION)
        registerReceiver(nativeReciever, intentFilter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(nativeReciever)
    }

    fun makeCall(view : View){
        val okHttpClient = OkHttpClient()
        when(view.id){
            R.id.btnNativeHttp ->{
                val intent = Intent(this, MyIntentService::class.java)
                intent.putExtra(KEY.CALLURL, INTENT_SERVICE_URL)
                startService(intent)
            }

            R.id.btnOkHttpSync ->{
                val syncRequest = Request.Builder()
                    .url(PERSON_BASE_URL)
                    .build()
                val response = okHttpClient.newCall(syncRequest).execute()
                val jsonObject = JSONObject(response.body()?.string())
                runOnUiThread{
                    try {
                        Toast.makeText(this@MainActivity, jsonObject.getString("name"), Toast.LENGTH_LONG).show()
                    }catch (e: JSONException){
                        e.printStackTrace()
                    }
                }
            }

            R.id.btnOkHttpAsync ->{
                val asyncRequest = Request.Builder().url(PERSON_BASE_URL).build()
                okHttpClient.newCall(asyncRequest).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        e.printStackTrace()
                    }
                    override fun onResponse(call: Call, response: Response) {
                        runOnUiThread {
                            try {
                                val gson = Gson()
                                val personJson = response.body()?.string()
                                val person = gson.fromJson(personJson, Person::class.java)
                                Log.d(TAG, "onResponse: " + person.toString())
                            } catch (e: IOException){
                                e.printStackTrace()
                            }
                            Toast.makeText(this@MainActivity, response.code().toString(), Toast.LENGTH_LONG).show()
                        }
                    }
                })
            }
            R.id.btnRetrofitSync ->{
                val gson = Gson()
                Thread {
                    try {
                        val response = RemoteServiceHelper.getWeatherData().execute()
                        val json = response.body()?.string()
                        val data = gson.fromJson(json, WeatherData::class.java)
                        Log.d(TAG, "City name: ${data.city.name} City Population ${data.city.population}")
                    }catch (e: IOException){
                        e.printStackTrace()
                    }
                }.start()
            }

            R.id.btnRetrofitAsync->{
                RemoteServiceHelper.getWeatherData().enqueue(object : retrofit2.Callback<ResponseBody> {
                    override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable) {
                        t.printStackTrace()
                    }

                    override fun onResponse(
                        call: retrofit2.Call<ResponseBody>,
                        response: retrofit2.Response<ResponseBody>
                    ) {
                        val gson = Gson()
                        val json = response.body()?.string()
                        val data = gson.fromJson(json, WeatherData::class.java)
                        Log.d(TAG, "City name: ${data.city.name} City Population ${data.city.population}")
                    }
                })
            }
        }
    }
}
