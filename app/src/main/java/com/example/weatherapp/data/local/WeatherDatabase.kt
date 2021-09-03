package com.example.weatherapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weatherapp.data.local.dao.WeatherDailyDao
import com.example.weatherapp.data.local.dao.WeatherHourlyDao
import com.example.weatherapp.data.local.dao.WeatherCurrentDao
import com.example.weatherapp.data.local.entity.WeatherDailyEntity
import com.example.weatherapp.data.local.entity.WeatherCurrentEntity
import com.example.weatherapp.data.local.entity.WeatherHourlyEntity

@Database(
    entities = [WeatherCurrentEntity::class,WeatherHourlyEntity::class,WeatherDailyEntity::class],
    version = 1
)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun getWeatherDao(): WeatherCurrentDao

    abstract fun getWeatherHourlyDao(): WeatherHourlyDao

    abstract fun getWeatherDailyDao(): WeatherDailyDao
}
