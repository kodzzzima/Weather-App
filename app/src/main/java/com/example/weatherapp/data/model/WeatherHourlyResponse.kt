package com.example.weatherapp.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName


data class WeatherHourlyResponse(
    @SerializedName("hourly")
    val hourly: List<Hourly>,
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lon")
    val lon: Double,
    @SerializedName("timezone")
    val timezone: String,
    @SerializedName("timezone_offset")
    val timezone_offset: Int
) {
    @Keep
    data class Hourly(
        val clouds: Int,
        val dew_point: Double,
        val dt: Int,
        val feels_like: Double,
        val humidity: Int,
        val pop: Double,
        val pressure: Int,
        val temp: Double,
        val uvi: Double,
        val visibility: Int,
        val weather: List<Weather>,
        val wind_deg: Int,
        val wind_gust: Double,
        val wind_speed: Double
    )

    @Keep
    data class Weather(
        val description: String,
        val icon: String,
        val id: Int,
        val main: String
    )
}