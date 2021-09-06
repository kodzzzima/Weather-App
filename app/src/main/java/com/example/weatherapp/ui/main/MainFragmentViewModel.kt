package com.example.weatherapp.ui.main

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.core.BaseViewModel
import com.example.weatherapp.data.ApiException
import com.example.weatherapp.data.NoInternetException
import com.example.weatherapp.data.local.entity.WeatherDailyEntity
import com.example.weatherapp.data.model.WeatherCurrentResponse
import com.example.weatherapp.data.local.entity.WeatherCurrentEntity
import com.example.weatherapp.data.local.entity.WeatherHourlyEntity
import com.example.weatherapp.data.model.WeatherDailyResponse
import com.example.weatherapp.data.model.WeatherHourlyResponse
import com.example.weatherapp.data.repo.WeatherCurrentRepository
import com.example.weatherapp.data.repo.WeatherHourlyRepository
import com.example.weatherapp.data.repo.WeatherDailyRepository
import com.example.weatherapp.util.Constants
import com.example.weatherapp.util.State
import com.example.weatherapp.util.Utils
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
) : BaseViewModel() {

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

    private fun getHourlyWeatherFromApi(){
        _weatherHourlyLiveData.postValue(State.loading())
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (lat?.isNotEmpty() == true && lon?.isNotEmpty() == true) {
                    weatherHourlyResponse = weatherHourlyRepository.getHourlyWeatherFromApi(lat!!, lon!!)
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

    private fun convertWeatherHourlyToEntity(weatherHourlyResponse: WeatherHourlyResponse):
            MutableList<WeatherHourlyEntity> {
        val weatherHourlyList = mutableListOf<WeatherHourlyEntity>()
        for (item in weatherHourlyResponse.hourly) {
            val weatherHourlyEntity = WeatherHourlyEntity(
                temp =item.temp,
                icon =item.weather[0].icon,
                dt = item.dt,
            )
            weatherHourlyList.add(weatherHourlyEntity)
        }
        return weatherHourlyList
    }

    fun fetchHourlyWeatherFromDb() {
        viewModelScope.launch(Dispatchers.IO) {
            val weatherHourly = weatherHourlyRepository.getHourlyWeatherFromDb()
            withContext(Dispatchers.Main) {
                if (weatherHourly.isNotEmpty()) {
                    if (true) {
                        weatherHourlyRepository.deleteAllWeatherFromDb()
                        getHourlyWeatherFromApi()
                    } else {
                        _weatherHourlyLiveData.postValue(
                            State.success(
                                weatherHourly
                            )
                        )
                    }
                } else getDailyWeatherFromApi()
            }
        }
    }

    private fun getCurrentWeatherFromApi(cityName: String) {
        _currentWeatherLiveData.postValue(State.loading())
        viewModelScope.launch(Dispatchers.IO) {
            try {
                weatherCurrentResponse = weatherCurrentRepository.getWeatherFromApi(cityName)
                withContext(Dispatchers.Main) {
                    val currentWeatherEntity =
                        convertCurrentWeatherResponseToEntity(weatherCurrentResponse)
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

    private fun saveCityNameAndCoordinates(city: String, lat: String, lon: String) {
        sharedPreferences.edit().putString(Constants.Coordinates.CITY, city).apply()
        sharedPreferences.edit().putString(Constants.Coordinates.LAT, lat).apply()
        sharedPreferences.edit().putString(Constants.Coordinates.LON, lon).apply()
    }

    fun fetchCurrentWeatherFromDb() {
        viewModelScope.launch(Dispatchers.IO) {
            val currentWeather = city?.let { weatherCurrentRepository.getCurrentWeatherFromDb(it) }
            withContext(Dispatchers.Main) {
                if (currentWeather != null) {
                    if (Utils.isTimeValid(currentWeather.dateTime)) {
                        city?.let { getCurrentWeatherFromApi(it) }
                    } else {
                        _currentWeatherLiveData.postValue(
                            State.success(
                                currentWeather
                            )
                        )
                    }

                } else {
                    city?.let { getCurrentWeatherFromApi(it) }
                }
            }
        }
    }

    private fun convertCurrentWeatherResponseToEntity(
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
        dateTime = Utils.getCurrentDateTime(Constants.Time.DATE_FORMAT_FULL)
    )
    
    private fun convertWeatherDailyToEntity(
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
}