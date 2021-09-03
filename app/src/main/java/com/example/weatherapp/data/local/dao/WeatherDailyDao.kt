package com.example.weatherapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.data.local.entity.WeatherDailyEntity

@Dao
interface WeatherDailyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllWeather(allWeatherList: List<WeatherDailyEntity>)

    @Query("DELETE FROM ${WeatherDailyEntity.TABLE_NAME}")
    suspend fun deleteAllWeather()

    @Query("SELECT * FROM ${WeatherDailyEntity.TABLE_NAME}")
    suspend fun getAllWeather(): List<WeatherDailyEntity>

}