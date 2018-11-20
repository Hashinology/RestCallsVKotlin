package com.example.headg.restcalls.models

data class WeatherData(val cod: String,
                       val message : Double,
                       val cnt: Int,
                       val list: List<WeatherList>,
                       val city: City)