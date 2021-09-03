package com.example.weatherapp.ui

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.core.BaseViewModel
import com.example.weatherapp.data.ApiException
import com.example.weatherapp.data.NoInternetException
import com.example.weatherapp.data.local.entity.WeatherDailyEntity
import com.example.weatherapp.data.model.WeatherDataResponse
import com.example.weatherapp.data.local.entity.WeatherDetailEntity
import com.example.weatherapp.data.model.WeatherDailyResponse
import com.example.weatherapp.data.model.WeatherHourlyResponse
import com.example.weatherapp.data.repo.CurrentWeatherRepository
import com.example.weatherapp.data.repo.HourlyForecastRepository
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
    private val curWeaRepository: CurrentWeatherRepository,
    private val hourlyRepository: HourlyForecastRepository,
    private val weatherDailyRepository: WeatherDailyRepository,
    var sharedPreferences: SharedPreferences
) : BaseViewModel() {

    private val _weatherLiveData =
        MutableLiveData<State<WeatherDetailEntity>>()
    val weatherLiveData: LiveData<State<WeatherDetailEntity>>
        get() = _weatherLiveData

    private lateinit var weatherResponse: WeatherDataResponse

    private val _weatherDailyLiveData =
        MutableLiveData<State<List<WeatherDailyEntity>>>()
    val weatherDailyLiveData: LiveData<State<List<WeatherDailyEntity>>>
        get() = _weatherDailyLiveData

    private lateinit var weatherDailyResponse: WeatherDailyResponse

    private val _weatherHourlyLiveData =
        MutableLiveData<State<WeatherHourlyResponse>>()
    val weatherHourlyLiveData: LiveData<State<WeatherHourlyResponse>>
        get() = _weatherHourlyLiveData

//    private lateinit var weatherHourlyResponse: WeatherHourlyResponse

    private fun findWeatherDaily() {
        val lat: String? =
            sharedPreferences.getString(Constants.Coords.LAT, Constants.Coords.LAT_DEFAULT)
        val lon: String? =
            sharedPreferences.getString(Constants.Coords.LON, Constants.Coords.LON_DEFAULT)

        _weatherDailyLiveData.postValue(State.loading())
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (lat?.isNotEmpty() == true && lon?.isNotEmpty() == true) {
                    weatherDailyResponse = weatherDailyRepository.getCityForecastDaily(lat, lon)
                }
                withContext(Dispatchers.Main) {
                    val weatherDailyToDb = convertWeatherDailyToEntity(weatherDailyResponse)
                    weatherDailyRepository.addAllWeather(weatherDailyToDb)
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


    fun fetchWeatherDailyFromDb() {
        viewModelScope.launch(Dispatchers.IO) {
            val weatherDaily = weatherDailyRepository.fetchWeatherDaily()
            withContext(Dispatchers.Main) {
                if (weatherDaily.isNotEmpty()) {
                    if (true) {
                        weatherDailyRepository.deleteAllWeather()
                        findWeatherDaily()
                    } else {
                        _weatherDailyLiveData.postValue(
                            State.success(
                                weatherDaily
                            )
                        )
                    }
                } else findWeatherDaily()
            }
        }

    }


    fun fetchHourlyWeatherFromDb() {

//        viewModelScope.launch {
//            sharedPreferences.edit().putString(Constants.Coords.LAT, "Moscow").apply()
//            _weatherHourlyLiveData.postValue(State.loading())
//            _weatherHourlyLiveData.postValue(
//                State.success(
//                    hourlyRepository.getCityForecastHourly(
//                        "33.44", "-94.04"
//                    )
//                )
//
//            )
//        }
    }

    private fun findCityWeather(cityName: String) {
        _weatherLiveData.postValue(State.loading())
        viewModelScope.launch(Dispatchers.IO) {
            try {
                weatherResponse = curWeaRepository.findCityWeather(cityName)
                withContext(Dispatchers.Main) {
                    val weatherDetail = convertWeatherResponseToEntity(weatherResponse)
                    curWeaRepository.addWeather(weatherDetail)
                    _weatherLiveData.postValue(
                        State.success(
                            weatherDetail
                        )
                    )
                }
            } catch (e: ApiException) {
                withContext(Dispatchers.Main) {
                    _weatherLiveData.postValue(State.error(e.message ?: ""))
                }
            } catch (e: NoInternetException) {
                withContext(Dispatchers.Main) {
                    _weatherLiveData.postValue(State.error(e.message ?: ""))
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _weatherLiveData.postValue(
                        State.error(
                            e.message ?: ""
                        )
                    )
                }
            }

        }
    }


    fun fetchWeatherDetailFromDb(cityName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val weatherDetail = curWeaRepository.fetchWeatherDetail(cityName.toLowerCase())
            withContext(Dispatchers.Main) {
                if (weatherDetail != null) {
                    if (Utils.isTimeValid(weatherDetail.dateTime)) {
                        findCityWeather(cityName)
                    } else {
                        _weatherLiveData.postValue(
                            State.success(
                                weatherDetail
                            )
                        )
                    }

                } else {
                    findCityWeather(cityName)
                }

            }
        }
    }

    private fun convertWeatherResponseToEntity(
        weatherResponse: WeatherDataResponse
    ): WeatherDetailEntity = WeatherDetailEntity(
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