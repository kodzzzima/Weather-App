package com.example.weatherapp.data.repo

import com.example.weatherapp.data.SafeApiRequest
import com.example.weatherapp.data.api.WeatherApi
import com.example.weatherapp.data.local.dao.WeatherDailyDao
import com.example.weatherapp.data.local.dao.WeatherDetailDao
import com.example.weatherapp.data.local.entity.WeatherDailyEntity
import com.example.weatherapp.data.local.entity.WeatherDetailEntity
import com.example.weatherapp.data.local.entity.WeatherHourlyEntity
import com.example.weatherapp.data.model.WeatherDailyResponse
import com.example.weatherapp.data.model.WeatherHourlyResponse
import javax.inject.Inject

class WeatherDailyRepository @Inject constructor(
    private val api: WeatherApi,
    private val db: WeatherDailyDao
): SafeApiRequest() {

    suspend fun getCityForecastDaily(lat:String,lon:String): WeatherDailyResponse = apiRequest {
        api.getCityWeatherDaily(lat, lon)
    }

    suspend fun fetchWeatherDaily(): List<WeatherDailyEntity> =
        db.fetchDailyWeather()

    suspend fun addAllWeather(weatherList: List<WeatherDailyEntity>) {
        db.addAllWeather(weatherList)
    }

    suspend fun deleteAllWeather() {
        db.deleteAllWeather()
    }
}