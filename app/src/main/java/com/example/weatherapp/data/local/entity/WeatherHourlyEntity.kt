package com.example.weatherapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weatherapp.data.local.entity.WeatherHourlyEntity.Companion.TABLE_NAME
import com.example.weatherapp.data.model.WeatherHourlyResponse

@Entity(tableName = TABLE_NAME)
data class WeatherHourlyEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val dt: Int? = null,
    val icon: String? = null,
    val temp: Double? = null,
) {
    companion object {
        const val TABLE_NAME = "weather_hourly"
    }
}