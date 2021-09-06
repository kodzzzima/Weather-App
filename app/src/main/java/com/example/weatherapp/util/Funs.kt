package com.example.weatherapp.util

import android.content.Context
import android.widget.Toast
import com.example.weatherapp.R

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun String?.convertToImageSource(): Int {
    var codeInteger = 2131165304
    when (this){
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
//fun covertImgCodeToSource(code: String?): Int {
//    var codeInteger = 2131165304
//    when (code) {
//        "01d", "01n" -> codeInteger = R.drawable.ic_clear_sky
//        "02d", "02n" -> codeInteger = R.drawable.ic_few_clouds
//        "03d", "03n" -> codeInteger = R.drawable.ic_scattered_clouds
//        "04n", "04d" -> codeInteger = R.drawable.ic_broken_clouds
//        "09d" -> codeInteger = R.drawable.ic_shower_rain
//        "50d" -> codeInteger = R.drawable.ic_mist
//        "10d" -> codeInteger = R.drawable.ic_rain
//        "11d" -> codeInteger = R.drawable.ic_thunderstorm
//        "13d" -> codeInteger = R.drawable.ic_snow
//    }
//    return codeInteger
//}