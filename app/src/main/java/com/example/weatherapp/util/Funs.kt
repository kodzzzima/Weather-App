package com.example.weatherapp.util

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import com.example.weatherapp.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToLong

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun String?.convertToImageSource(): Int {
    var codeInteger = 2131165304
    when (this) {
        "01d", "01n" -> codeInteger = R.drawable.ic_clear_sky
        "02d", "02n" -> codeInteger = R.drawable.ic_few_clouds
        "03d", "03n" -> codeInteger = R.drawable.ic_scattered_clouds
        "04n", "04d" -> codeInteger = R.drawable.ic_broken_clouds
        "09d" -> codeInteger = R.drawable.ic_shower_rain
        "50d" -> codeInteger = R.drawable.ic_mist
        "10d" -> codeInteger = R.drawable.ic_rain
        "11d" -> codeInteger = R.drawable.ic_thunderstorm
        "13d" -> codeInteger = R.drawable.ic_snow
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


