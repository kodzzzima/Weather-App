package com.example.weatherapp.data.api

import com.example.weatherapp.data.model.WeatherDailyResponse
import com.example.weatherapp.data.model.WeatherCurrentResponse
import com.example.weatherapp.data.model.WeatherHourlyResponse
import com.example.weatherapp.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("weather")
    suspend fun findCityWeatherData(
        @Query("q") q: String,
        @Query("lang") lang: String = Constants.Network.LANGUAGE,
        @Query("units") units: String = Constants.Network.WEATHER_UNIT,
        @Query("appid") appid: String = Constants.Network.WEATHER_API_KEY
    ): Response<WeatherCurrentResponse>

    @GET("onecall")
    suspend fun getCityForecastHourly(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("lang") lang: String = Constants.Network.LANGUAGE,
        @Query("exclude") exclude: String = Constants.Network.EXCLUDE_FOR_HOURLY,
        @Query("units") units: String = Constants.Network.WEATHER_UNIT,
        @Query("appid") appid: String = Constants.Network.WEATHER_API_KEY
    ): Response<WeatherHourlyResponse>

    @GET("onecall")
    suspend fun getCityWeatherDaily(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("lang") lang: String = Constants.Network.LANGUAGE,
        @Query("exclude") exclude: String = Constants.Network.EXCLUDE_FOR_DAILY,
        @Query("units") units: String = Constants.Network.WEATHER_UNIT,
        @Query("appid") appid: String = Constants.Network.WEATHER_API_KEY
    ): Response<WeatherDailyResponse>
}