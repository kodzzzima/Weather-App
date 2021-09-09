package com.example.weatherapp.util

import com.example.weatherapp.data.local.entity.WeatherCurrentEntity
import com.example.weatherapp.data.local.entity.WeatherDailyEntity
import com.example.weatherapp.data.local.entity.WeatherHourlyEntity
import com.example.weatherapp.data.model.WeatherCurrentResponse
import com.example.weatherapp.data.model.WeatherDailyResponse
import com.example.weatherapp.data.model.WeatherHourlyResponse

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
    dateTime = weatherResponse.dt.toString()
)

fun WeatherDailyResponse.toEntity(
): List<WeatherDailyEntity> =
    daily.map { daily ->
        WeatherDailyEntity(
            tempDay = daily.temp.day,
            tempNight = daily.temp.night,
            icon = daily.weather[0].icon,
            description = daily.weather[0].description,
            dt = daily.dt,
        )
    }

fun WeatherHourlyResponse.toEntity():
        List<WeatherHourlyEntity> =
    hourly.map { hourly ->
        WeatherHourlyEntity(
            temp = hourly.temp,
            icon = hourly.weather[0].icon,
            dt = hourly.dt
        )
    }

fun WeatherHourlyResponse.convert(): List<WeatherHourlyEntity> {
    val list = mutableListOf<WeatherHourlyEntity>()
    hourly.forEachIndexed { index, hourly ->
        list.add(
            WeatherHourlyEntity(
                id = index,
                temp = hourly.temp,
                icon = hourly.weather[0].icon,
                dt = hourly.dt
            )
        )
    }
    return list.toList()
}
