package com.example.weatherapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.data.local.entity.WeatherCurrentEntity

@Dao
interface WeatherCurrentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addWeather(weatherDetailEntity: WeatherCurrentEntity)

    @Query("SELECT * FROM ${WeatherCurrentEntity.TABLE_NAME} WHERE cityName = :cityName")
    suspend fun getWeatherByCity(cityName: String): WeatherCurrentEntity?

}
