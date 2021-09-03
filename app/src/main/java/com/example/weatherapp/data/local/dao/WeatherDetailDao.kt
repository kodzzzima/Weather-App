package com.example.weatherapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.data.local.entity.WeatherDetailEntity

@Dao
interface WeatherDetailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addWeather(weatherDetailEntity: WeatherDetailEntity)

    @Query("SELECT * FROM ${WeatherDetailEntity.TABLE_NAME} WHERE cityName = :cityName")
    suspend fun fetchWeatherByCity(cityName: String): WeatherDetailEntity?

}
