package com.example.weatherapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.data.local.entity.WeatherHourlyEntity

@Dao
interface WeatherHourlyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addWeather(hourlyEntity: WeatherHourlyEntity)

    @Query("SELECT * FROM ${WeatherHourlyEntity.TABLE_NAME}")
    suspend fun fetchHourlyForecast(): WeatherHourlyEntity?
}