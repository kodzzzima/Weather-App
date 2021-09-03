package com.example.weatherapp.util

import android.annotation.SuppressLint
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

}