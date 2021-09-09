package com.example.weatherapp.data.repo

import com.example.weatherapp.data.SafeApiRequest
import com.example.weatherapp.data.api.WeatherApi
import com.example.weatherapp.data.local.dao.WeatherCurrentDao
import com.example.weatherapp.data.model.WeatherCurrentResponse
import com.example.weatherapp.data.local.entity.WeatherCurrentEntity
import javax.inject.Inject

class WeatherCurrentRepository @Inject constructor(
    private val api: WeatherApi,
    private val db: WeatherCurrentDao
) : SafeApiRequest() {

    suspend fun getWeatherFromApi(cityName: String): WeatherCurrentResponse = apiRequest {
        api.findCityWeatherData(cityName)
    }

    suspend fun addCurrentWeatherToDb(weatherDetail: WeatherCurrentEntity) {
        db.addWeather(weatherDetail)
    }

    suspend fun getCurrentWeatherFromDb(cityName: String): WeatherCurrentEntity? =
        db.getWeatherByCity(cityName)
}