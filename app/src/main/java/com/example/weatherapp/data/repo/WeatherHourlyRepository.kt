package com.example.weatherapp.data.repo

import com.example.weatherapp.data.SafeApiRequest
import com.example.weatherapp.data.api.WeatherApi
import com.example.weatherapp.data.local.dao.WeatherHourlyDao
import com.example.weatherapp.data.local.entity.WeatherDailyEntity
import com.example.weatherapp.data.local.entity.WeatherHourlyEntity
import com.example.weatherapp.data.model.WeatherHourlyResponse
import javax.inject.Inject

class WeatherHourlyRepository @Inject constructor(
    private val api: WeatherApi,
    private val db: WeatherHourlyDao,
): SafeApiRequest() {

    suspend fun getHourlyWeatherFromApi(lat:String, lon:String): WeatherHourlyResponse = apiRequest {
        api.getCityForecastHourly(lat, lon)
    }

    suspend fun getHourlyWeatherFromDb(): List<WeatherHourlyEntity> =
        db.getAllWeather()

    suspend fun addHourlyWeatherToDb(weatherList: List<WeatherHourlyEntity>) {
        db.addAllWeather(weatherList)
    }

    suspend fun deleteAllWeatherFromDb() {
        db.deleteAllWeather()
    }

}