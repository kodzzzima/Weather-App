package com.example.weatherapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weatherapp.data.local.entity.WeatherHourlyEntity.Companion.TABLE_NAME
import com.example.weatherapp.data.model.WeatherHourlyResponse

@Entity(tableName = TABLE_NAME)
data class WeatherHourlyEntity(

    @PrimaryKey
    var id: Int? = 0,
//    val hourly: List<Hourly>? = null,
    val lat: Double? = null,
    val lon: Double? = null,
    val timezone: String? = null,
) {
    companion object {
        const val TABLE_NAME = "weather_hourly"
    }

//    data class Hourly(
//        val clouds: Int,
//        val dew_point: Double,
//        val dt: Int,
//        val feels_like: Double,
//        val humidity: Int,
//        val pop: Double,
//        val pressure: Int,
//        val temp: Double,
//        val uvi: Double,
//        val visibility: Int,
//        val weather: List<Weather>,
//        val wind_deg: Int,
//        val wind_gust: Double,
//        val wind_speed: Double
//    )
//
//    data class Weather(
//        val description: String,
//        val icon: String,
//        val id: Int,
//        val main: String
//    )
}