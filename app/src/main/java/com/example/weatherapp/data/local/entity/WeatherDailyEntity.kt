package com.example.weatherapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = WeatherDailyEntity.TABLE_NAME)
class WeatherDailyEntity(

    @PrimaryKey
    var id: Int? = 0,
    var tempDay: Double? = null,
    var tempNight: Double? = null,
    var icon: String? = null,
    var description: String? = null,
    var dt: Int? = null,
) {
    companion object {
        const val TABLE_NAME = "weather_daily"
    }
}