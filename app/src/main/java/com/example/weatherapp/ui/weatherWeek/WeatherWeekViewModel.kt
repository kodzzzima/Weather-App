package com.example.weatherapp.ui.weatherWeek

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.ApiException
import com.example.weatherapp.data.NoInternetException
import com.example.weatherapp.data.local.entity.WeatherDailyEntity
import com.example.weatherapp.data.model.WeatherDailyResponse
import com.example.weatherapp.data.repo.WeatherDailyRepository
import com.example.weatherapp.util.Constants
import com.example.weatherapp.util.State
import com.example.weatherapp.util.Utils.convertWeatherDailyToEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class WeatherWeekViewModel @Inject internal constructor(
    private val weatherDailyRepository: WeatherDailyRepository,
    private var sharedPreferences: SharedPreferences
): ViewModel() {
    private val _weatherDailyLiveData =
        MutableLiveData<State<List<WeatherDailyEntity>>>()
    val weatherDailyLiveData: LiveData<State<List<WeatherDailyEntity>>>
        get() = _weatherDailyLiveData

    private lateinit var weatherDailyResponse: WeatherDailyResponse

    private var lat: String? =
        sharedPreferences.getString(Constants.Coordinates.LAT, Constants.Coordinates.LAT_DEFAULT)
    private var lon: String? =
        sharedPreferences.getString(Constants.Coordinates.LON, Constants.Coordinates.LON_DEFAULT)
    private var city: String? =
        sharedPreferences.getString(Constants.Coordinates.CITY, Constants.Coordinates.CITY_DEFAULT)

    private fun getDailyWeatherFromApi() {
        _weatherDailyLiveData.postValue(State.loading())
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (lat?.isNotEmpty() == true && lon?.isNotEmpty() == true) {
                    weatherDailyResponse = weatherDailyRepository.getDailyWeatherFromApi(lat!!, lon!!)
                }
                withContext(Dispatchers.Main) {
                    val weatherDailyToDb = convertWeatherDailyToEntity(weatherDailyResponse)
                    weatherDailyRepository.addDailyWeatherToDb(weatherDailyToDb)
                    _weatherDailyLiveData.postValue(
                        State.success(
                            weatherDailyToDb
                        )
                    )
                }
            } catch (e: ApiException) {
                withContext(Dispatchers.Main) {
                    _weatherDailyLiveData.postValue(State.error(e.message ?: ""))
                }
            } catch (e: NoInternetException) {
                withContext(Dispatchers.Main) {
                    _weatherDailyLiveData.postValue(State.error(e.message ?: ""))
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _weatherDailyLiveData.postValue(
                        State.error(
                            e.message ?: ""
                        )
                    )
                }
            }
        }
    }

    fun fetchDailyWeatherFromDb() {
        viewModelScope.launch(Dispatchers.IO) {
            val weatherDaily = weatherDailyRepository.getDailyWeatherFromDb()
            withContext(Dispatchers.Main) {
                if (weatherDaily.isNotEmpty()) {
                    if (true) {
                        weatherDailyRepository.deleteAllWeatherFromDb()
                        getDailyWeatherFromApi()
                    } else {
                        _weatherDailyLiveData.postValue(
                            State.success(
                                weatherDaily
                            )
                        )
                    }
                } else getDailyWeatherFromApi()
            }
        }

    }


}