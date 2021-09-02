package com.example.weatherapp.data.repo

import com.example.weatherapp.data.SafeApiRequest
import com.example.weatherapp.data.api.WeatherApi
import com.example.weatherapp.data.local.WeatherDatabase
import com.example.weatherapp.data.local.WeatherDetailDao
import com.example.weatherapp.data.model.WeatherDataResponse
import com.example.weatherapp.data.model.WeatherDetail
import com.example.weatherapp.data.model.WeatherHourly
import javax.inject.Inject

class CurrentWeatherRepository @Inject constructor(
    private val api: WeatherApi,
    private val db: WeatherDetailDao
):SafeApiRequest() {

    suspend fun findCityWeather(cityName: String): WeatherDataResponse = apiRequest {
        api.findCityWeatherData(cityName)
    }

    suspend fun getCityForecastHourly(lat:String,lon:String): WeatherHourly = apiRequest {
            api.getCityForecastHourly(lat, lon)
        }

    suspend fun addWeather(weatherDetail: WeatherDetail) {
        db.addWeather(weatherDetail)
    }

    suspend fun fetchWeatherDetail(cityName: String): WeatherDetail? =
        db.fetchWeatherByCity(cityName)

}