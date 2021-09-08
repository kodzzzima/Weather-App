package com.example.weatherapp.util

import android.annotation.SuppressLint
import com.example.weatherapp.data.local.entity.WeatherCurrentEntity
import com.example.weatherapp.data.local.entity.WeatherDailyEntity
import com.example.weatherapp.data.local.entity.WeatherHourlyEntity
import com.example.weatherapp.data.model.WeatherCurrentResponse
import com.example.weatherapp.data.model.WeatherDailyResponse
import com.example.weatherapp.data.model.WeatherHourlyResponse
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    @SuppressLint("SimpleDateFormat")
    fun getCurrentDateTime(dateFormat: String): String =
        SimpleDateFormat(dateFormat).format(Date())

    @SuppressLint("SimpleDateFormat")
    fun isTimeValid(dateTimeSavedWeather: String?): Boolean{
        dateTimeSavedWeather?.let {
            val currentDateTime = Date()
            val savedWeatherDateTime =
                SimpleDateFormat(Constants.Time.DATE_FORMAT_FULL).parse(it)
            val diff: Long = currentDateTime.time - savedWeatherDateTime.time
            val seconds = diff / 1000
            val minutes = seconds / 60
            if (minutes > 10)
                return true
        }
        return false
    }

    fun convertCurrentWeatherResponseToEntity(
        weatherResponse: WeatherCurrentResponse
    ): WeatherCurrentEntity = WeatherCurrentEntity(
        description = weatherResponse.weather[0].description,
        icon = weatherResponse.weather.first().icon,
        cityName = weatherResponse.name,
        countryName = weatherResponse.sys.country,
        temp = weatherResponse.main.temp,
        feelsLike = weatherResponse.main.feelsLike,
        lat = weatherResponse.coord.lat,
        lon = weatherResponse.coord.lon,
        pressure = weatherResponse.main.pressure,
        humidity = weatherResponse.main.humidity,
        wind_speed = weatherResponse.wind.speed,
        dateTime = getCurrentDateTime(Constants.Time.DATE_FORMAT_FULL)
    )

    fun convertWeatherDailyToEntity(
        weatherDailyResponse: WeatherDailyResponse
    ): MutableList<WeatherDailyEntity> {
        val weatherDailyList = mutableListOf<WeatherDailyEntity>()
        for (item in weatherDailyResponse.daily) {
            val weatherDailyEntity = WeatherDailyEntity(
                tempDay = item.temp.day,
                tempNight = item.temp.night,
                icon = item.weather[0].icon,
                description = item.weather[0].description,
                dt = item.dt,
            )
            weatherDailyList.add(weatherDailyEntity)
        }
        return weatherDailyList
    }

    fun convertWeatherHourlyToEntity(weatherHourlyResponse: WeatherHourlyResponse):
            MutableList<WeatherHourlyEntity> {
        val weatherHourlyList = mutableListOf<WeatherHourlyEntity>()
        for (item in weatherHourlyResponse.hourly) {
            val weatherHourlyEntity = WeatherHourlyEntity(
                temp = item.temp,
                icon = item.weather[0].icon,
                dt = item.dt,
            )
            weatherHourlyList.add(weatherHourlyEntity)
        }
        return weatherHourlyList
    }


}