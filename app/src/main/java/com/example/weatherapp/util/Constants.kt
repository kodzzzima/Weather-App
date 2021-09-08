package com.example.weatherapp.util

object Constants {
    object Network {
        const val BASE_URL = "http://api.openweathermap.org/data/2.5/"
        const val LANGUAGE = "ru"
        const val WEATHER_UNIT = "metric"
        const val WEATHER_API_KEY = "85b82272350e8fb22b29c06613c42aea"
        const val EXCLUDE_FOR_HOURLY = "current,daily,minutely"
        const val EXCLUDE_FOR_DAILY = "current,minutely,hourly"
    }

    object Preferences {
        const val LAT = "lat"
        const val LON = "lon"
        const val CITY = "city"
        const val DATE = "date"
        const val CITY_DEFAULT = "Москва"
        const val LAT_DEFAULT = "55.751244"
        const val LON_DEFAULT = "37.618423"
    }

    object Time {
        const val DATE_FORMAT = "E, d MMM yyyy"
        const val DATE_FORMAT_FULL = "E, d MMM yyyy HH:mm:ss"
    }
}