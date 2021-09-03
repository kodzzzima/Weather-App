package com.example.weatherapp.data.repo

import com.example.weatherapp.data.SafeApiRequest
import com.example.weatherapp.data.api.WeatherApi
import com.example.weatherapp.data.local.dao.WeatherHourlyDao
import com.example.weatherapp.data.local.entity.WeatherHourlyEntity
import com.example.weatherapp.data.model.WeatherHourlyResponse
import javax.inject.Inject

class HourlyForecastRepository @Inject constructor(
    private val api: WeatherApi,
    private val db: WeatherHourlyDao,
): SafeApiRequest() {


    suspend fun getCityForecastHourly(lat:String,lon:String): WeatherHourlyResponse = apiRequest {
        api.getCityForecastHourly(lat, lon)
    }

    suspend fun fetchCityForecastHourly(): WeatherHourlyEntity? =
        db.fetchHourlyForecast()

}