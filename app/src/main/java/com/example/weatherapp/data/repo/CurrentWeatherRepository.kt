package com.example.weatherapp.data.repo

import com.example.weatherapp.data.SafeApiRequest
import com.example.weatherapp.data.api.WeatherApi
import com.example.weatherapp.data.local.dao.WeatherDetailDao
import com.example.weatherapp.data.model.WeatherDataResponse
import com.example.weatherapp.data.local.entity.WeatherDetailEntity
import javax.inject.Inject

class CurrentWeatherRepository @Inject constructor(
    private val api: WeatherApi,
    private val db: WeatherDetailDao
):SafeApiRequest() {

    suspend fun findCityWeather(cityName: String): WeatherDataResponse = apiRequest {
        api.findCityWeatherData(cityName)
    }

    suspend fun addWeather(weatherDetail: WeatherDetailEntity) {
        db.addWeather(weatherDetail)
    }

    suspend fun fetchWeatherDetail(cityName: String): WeatherDetailEntity? =
        db.fetchWeatherByCity(cityName)

}