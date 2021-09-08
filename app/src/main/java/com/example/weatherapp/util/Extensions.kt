package com.example.weatherapp.util

import android.annotation.SuppressLint
import com.example.weatherapp.R
import com.example.weatherapp.data.local.entity.WeatherCurrentEntity
import com.example.weatherapp.data.local.entity.WeatherDailyEntity
import com.example.weatherapp.data.local.entity.WeatherHourlyEntity
import com.example.weatherapp.data.model.WeatherCurrentResponse
import com.example.weatherapp.data.model.WeatherDailyResponse
import com.example.weatherapp.data.model.WeatherHourlyResponse
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt
import kotlin.math.roundToLong

fun String?.convertToImageSource(): Int {
    var codeInteger = 2131165290
    when (this) {
        "01d", "01n" -> codeInteger = R.drawable.ic_clear_sky
        "02d", "02n" -> codeInteger = R.drawable.ic_few_clouds
        "03d", "03n" -> codeInteger = R.drawable.ic_scattered_clouds
        "04n", "04d" -> codeInteger = R.drawable.ic_broken_clouds
        "09d", "09n" -> codeInteger = R.drawable.ic_shower_rain
        "50d", "50n" -> codeInteger = R.drawable.ic_mist
        "10d", "10n" -> codeInteger = R.drawable.ic_rain
        "11d", "11n" -> codeInteger = R.drawable.ic_thunderstorm
        "13d", "13n" -> codeInteger = R.drawable.ic_snow
    }
    return codeInteger
}

fun String.titleCaseFirstChar() = replaceFirstChar(Char::titlecase)

@SuppressLint("SimpleDateFormat")
fun Int?.getDayOfWeek(): String {
    return try {
        val sdf = SimpleDateFormat("EEEE")
        val netDate = Date(this?.toLong()!! * 1000)
        sdf.format(netDate).replaceFirstChar(Char::titlecase)
    } catch (e: Exception) {
        e.toString()
    }
}

fun Double.roundTemperatureAndGetString(): String =
    this.roundToLong().toString() + "°"

@SuppressLint("SimpleDateFormat")
fun Int?.getDayAndMonth(): String {
    return try {
        val sdf = SimpleDateFormat("dd.MM")
        val netDate = Date(this?.toLong()!! * 1000)
        sdf.format(netDate)
    } catch (e: Exception) {
        e.toString()
    }
}

@SuppressLint("SimpleDateFormat")
fun Int?.getTimeInHoursAndMinutes(): String {
    return try {
        val sdf = SimpleDateFormat("HH:mm")
        val netDate = Date(this?.toLong()!! * 1000)
        sdf.format(netDate)
    } catch (e: Exception) {
        e.toString()
    }
}

fun Int?.pressureConvertToMM(): String {
    val num = (this!! * 0.767).roundToInt()
    return "$num мм рт ст "
}




