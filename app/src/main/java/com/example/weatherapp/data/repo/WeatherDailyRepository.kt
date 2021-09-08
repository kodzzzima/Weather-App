package com.example.weatherapp.data.repo

import android.util.Log
import com.example.weatherapp.data.SafeApiRequest
import com.example.weatherapp.data.api.WeatherApi
import com.example.weatherapp.data.local.dao.WeatherDailyDao
import com.example.weatherapp.data.local.entity.WeatherDailyEntity
import com.example.weatherapp.data.model.WeatherDailyResponse
import javax.inject.Inject

class WeatherDailyRepository @Inject constructor(
    private val api: WeatherApi,
    private val db: WeatherDailyDao
): SafeApiRequest() {

    suspend fun getDailyWeatherFromApi(lat:String, lon:String): WeatherDailyResponse = apiRequest {
        api.getCityWeatherDaily(lat, lon)
    }

    suspend fun getDailyWeatherFromDb(): List<WeatherDailyEntity> =
        db.getAllWeather()

    suspend fun addDailyWeatherToDb(weatherList: List<WeatherDailyEntity>) {
        for (item in weatherList){
            Log.d("testLog",item.id.toString())
        }
        db.addAllWeather(weatherList)
    }

    suspend fun deleteAllWeatherFromDb() {
        db.deleteAllWeather()
    }
}