package com.example.weatherapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weatherapp.data.local.entity.WeatherHourlyEntity.Companion.TABLE_NAME
import com.example.weatherapp.data.model.WeatherHourlyResponse

@Entity(tableName = TABLE_NAME)
data class WeatherHourlyEntity(

    @PrimaryKey
    var id: Int? = 0,
    var dt: Int? = null,
    var icon: String? = null,
    var temp: Double? = null,
) {
    companion object {
        const val TABLE_NAME = "weather_hourly"
    }
}