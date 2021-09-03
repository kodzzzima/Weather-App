package com.example.weatherapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weatherapp.data.local.entity.WeatherDetailEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class WeatherDetailEntity(

    @PrimaryKey
    var id: Int? = 0,
    var temp: Double? = null,
    var icon: String? = null,
    var cityName: String? = null,
    var countryName: String? = null,
    var dateTime: String? = null,
    var description: String? = null,
    var feelsLike: Double? = null,
    var humidity: Int? = null,
    var pressure: Int? = null,
    var wind_speed: Double? = null,
    var lat: Double? = null,
    var lon: Double? = null,
) {
    companion object {
        const val TABLE_NAME = "weather_detail"
    }
}

