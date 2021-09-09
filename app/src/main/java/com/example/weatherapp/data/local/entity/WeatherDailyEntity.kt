package com.example.weatherapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = WeatherDailyEntity.TABLE_NAME)
data class WeatherDailyEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val tempDay: Double? = null,
    val tempNight: Double? = null,
    val icon: String? = null,
    val description: String? = null,
    val dt: Int? = null,
) {
    companion object {
        const val TABLE_NAME = "weather_daily"
    }
}