package com.example.weatherapp.ui.main

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.ApiException
import com.example.weatherapp.data.NoInternetException
import com.example.weatherapp.data.local.entity.WeatherCurrentEntity
import com.example.weatherapp.data.local.entity.WeatherDailyEntity
import com.example.weatherapp.data.local.entity.WeatherHourlyEntity
import com.example.weatherapp.data.model.WeatherCurrentResponse
import com.example.weatherapp.data.model.WeatherDailyResponse
import com.example.weatherapp.data.model.WeatherHourlyResponse
import com.example.weatherapp.data.repo.WeatherCurrentRepository
import com.example.weatherapp.data.repo.WeatherDailyRepository
import com.example.weatherapp.data.repo.WeatherHourlyRepository
import com.example.weatherapp.util.Constants
import com.example.weatherapp.util.State
import com.example.weatherapp.util.Utils
import com.example.weatherapp.util.Utils.convertCurrentWeatherResponseToEntity
import com.example.weatherapp.util.Utils.convertWeatherDailyToEntity
import com.example.weatherapp.util.Utils.convertWeatherHourlyToEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject internal constructor(
    private val weatherCurrentRepository: WeatherCurrentRepository,
    private val weatherHourlyRepository: WeatherHourlyRepository,
    private val weatherDailyRepository: WeatherDailyRepository,
    private var sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _currentWeatherLiveData =
        MutableLiveData<State<WeatherCurrentEntity>>()
    val currentWeatherLiveData: LiveData<State<WeatherCurrentEntity>>
        get() = _currentWeatherLiveData

    private lateinit var weatherCurrentResponse: WeatherCurrentResponse

    private val _weatherDailyLiveData =
        MutableLiveData<State<List<WeatherDailyEntity>>>()
    val weatherDailyLiveData: LiveData<State<List<WeatherDailyEntity>>>
        get() = _weatherDailyLiveData

    private lateinit var weatherDailyResponse: WeatherDailyResponse

    private val _weatherHourlyLiveData =
        MutableLiveData<State<List<WeatherHourlyEntity>>>()
    val weatherHourlyLiveData: LiveData<State<List<WeatherHourlyEntity>>>
        get() = _weatherHourlyLiveData

    private lateinit var weatherHourlyResponse: WeatherHourlyResponse

    private val _latAndLonLiveData =
        MutableLiveData<List<String>>()
    val latAndLonLiveData: LiveData<List<String>>
        get() = _latAndLonLiveData

    var latPrefs: String? =
        sharedPreferences.getString(Constants.Preferences.LAT, Constants.Preferences.LAT_DEFAULT)
    var lonPrefs: String? =
        sharedPreferences.getString(Constants.Preferences.LON, Constants.Preferences.LON_DEFAULT)
    var cityPrefs: String? =
        sharedPreferences.getString(Constants.Preferences.CITY, Constants.Preferences.CITY_DEFAULT)
    var datePrefs: String? =
        sharedPreferences.getString(Constants.Preferences.DATE, Utils.getCurrentDateTime(Constants.Time.DATE_FORMAT_FULL))

    private fun saveLastResponseTime() {
        sharedPreferences.edit().putString(
            Constants.Preferences.DATE,
            Utils.getCurrentDateTime(Constants.Time.DATE_FORMAT_FULL)
        ).apply()
    }


    fun getCurrentWeatherFromApi(cityName: String?) {
        _currentWeatherLiveData.postValue(State.loading())
        viewModelScope.launch(Dispatchers.IO) {
            try {
                weatherCurrentResponse = cityName?.let {
                    weatherCurrentRepository.getWeatherFromApi(
                        it
                    )
                }!!
                saveLastResponseTime()
                saveCityNameAndCoordinates(
                    cityName,
                    weatherCurrentResponse.coord.lat.toString(),
                    weatherCurrentResponse.coord.lon.toString()
                )

                withContext(Dispatchers.Main) {
                    val currentWeatherEntity =
                        convertCurrentWeatherResponseToEntity(weatherCurrentResponse)
                    Log.d("testLog", currentWeatherEntity.dateTime.toString() + "dada")
                    weatherCurrentRepository.addCurrentWeatherToDb(currentWeatherEntity)
                    _currentWeatherLiveData.postValue(
                        State.success(
                            currentWeatherEntity
                        )
                    )
                }
            } catch (e: ApiException) {
                withContext(Dispatchers.Main) {
                    _currentWeatherLiveData.postValue(State.error(e.message ?: ""))
                }
            } catch (e: NoInternetException) {
                withContext(Dispatchers.Main) {
                    _currentWeatherLiveData.postValue(State.error(e.message ?: ""))
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _currentWeatherLiveData.postValue(
                        State.error(
                            e.message ?: ""
                        )
                    )
                }
            }
        }
    }

    fun getDailyWeatherFromApi(lat: String?, lon: String?) {
        _weatherDailyLiveData.postValue(State.loading())
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (lat?.isNotEmpty() == true && lon?.isNotEmpty() == true) {
                    weatherDailyResponse =
                        weatherDailyRepository.getDailyWeatherFromApi(lat, lon)
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

    fun getHourlyWeatherFromApi(lat: String?, lon: String?) {
        _weatherHourlyLiveData.postValue(State.loading())
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (lat?.isNotEmpty() == true && lon?.isNotEmpty() == true) {
                    weatherHourlyResponse =
                        weatherHourlyRepository.getHourlyWeatherFromApi(lat, lon)
                }
                withContext(Dispatchers.Main) {
                    val weatherHourlyToDb = convertWeatherHourlyToEntity(weatherHourlyResponse)
                    weatherHourlyRepository.addHourlyWeatherToDb(weatherHourlyToDb)
                    _weatherHourlyLiveData.postValue(
                        State.success(
                            weatherHourlyToDb
                        )
                    )
                }
            } catch (e: ApiException) {
                withContext(Dispatchers.Main) {
                    _weatherHourlyLiveData.postValue(State.error(e.message ?: ""))
                }
            } catch (e: NoInternetException) {
                withContext(Dispatchers.Main) {
                    _weatherHourlyLiveData.postValue(State.error(e.message ?: ""))
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _weatherHourlyLiveData.postValue(
                        State.error(
                            e.message ?: ""
                        )
                    )
                }
            }
        }
    }

    fun fetchDailyWeatherFromDb(lat: String?, lon: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            val weatherDaily = weatherDailyRepository.getDailyWeatherFromDb()
            withContext(Dispatchers.Main) {
                if (weatherDaily.isNotEmpty()) {
                    _weatherDailyLiveData.postValue(
                        State.success(
                            weatherDaily
                        )
                    )
                    if (true) {
                        weatherDailyRepository.deleteAllWeatherFromDb()
                        getDailyWeatherFromApi(lat, lon)
                    }
                } else getDailyWeatherFromApi(lat, lon)
            }
        }

    }

    fun fetchHourlyWeatherFromDb(lat: String?, lon: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            val weatherHourly = weatherHourlyRepository.getHourlyWeatherFromDb()
            withContext(Dispatchers.Main) {
                if (weatherHourly.isNotEmpty()) {
                    _weatherHourlyLiveData.postValue(
                        State.success(
                            weatherHourly
                        )
                    )
                    if (true) {
                        weatherHourlyRepository.deleteAllWeatherFromDb()
                        getHourlyWeatherFromApi(lat, lon)
                    }
                } else getHourlyWeatherFromApi(lat, lon)
            }
        }
    }




    fun fetchCurrentWeatherFromDb(cityName: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentWeather =
                cityName?.let { weatherCurrentRepository.getCurrentWeatherFromDb(it) }
            withContext(Dispatchers.Main) {
                if (currentWeather != null) {
                    Log.d("testLog",datePrefs.toString())
                    _currentWeatherLiveData.postValue(
                        State.success(
                            currentWeather
                        )
                    )
                    if (Utils.isTimeValid(datePrefs)) {
                        Log.d("testLog",datePrefs.toString())
                        Log.d("testLog","valid")
                        getCurrentWeatherFromApi(cityName)
                    }
                } else {
                    getCurrentWeatherFromApi(cityName)
                }
            }
        }
    }

    fun saveCityNameAndCoordinates(city: String, lat: String, lon: String) {
        sharedPreferences.edit().putString(Constants.Preferences.CITY, city).apply()
        sharedPreferences.edit().putString(Constants.Preferences.LAT, lat).apply()
        sharedPreferences.edit().putString(Constants.Preferences.LON, lon).apply()

        updateLatLonLiveData(lat,lon)
    }

    private fun updateLatLonLiveData(lat: String, lon: String) {
        val listOfLatLon = listOf(lat,lon)
        _latAndLonLiveData.postValue(listOfLatLon)

    }
}