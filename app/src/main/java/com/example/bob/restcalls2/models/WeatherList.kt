package com.example.headg.restcalls.models

import com.google.gson.annotations.SerializedName

data class WeatherList(val dt: Int,
                       val main: Main,
                       val weather: List<Weather>,
                      val clouds : Clouds,
                        val wind : Wind,
                        val rain : Rain,
                        val sys : Sys,
                        @SerializedName("dt_text") val dtTxt : String)
