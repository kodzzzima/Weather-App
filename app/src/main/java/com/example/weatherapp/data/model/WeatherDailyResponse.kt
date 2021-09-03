package com.example.weatherapp.data.model

data class WeatherDailyResponse(
    val daily: List<Daily>,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Int
)