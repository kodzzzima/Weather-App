package com.example.weatherapp.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.core.BaseViewModel
import com.example.weatherapp.data.ApiException
import com.example.weatherapp.data.NoInternetException
import com.example.weatherapp.data.model.WeatherDataResponse
import com.example.weatherapp.data.model.WeatherDetail
import com.example.weatherapp.data.model.WeatherHourly
import com.example.weatherapp.data.repo.CurrentWeatherRepository
import com.example.weatherapp.util.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject internal constructor(
    private val repository: CurrentWeatherRepository
) : BaseViewModel() {

    private val _weatherLiveData =
        MutableLiveData<State<WeatherDetail>>()
    val weatherLiveData: LiveData<State<WeatherDetail>>
        get() = _weatherLiveData

    private lateinit var weatherResponse: WeatherDataResponse

    private val _weatherHourlyLiveData =
        MutableLiveData<State<WeatherHourly>>()
    val weatherHourlyLiveData: LiveData<State<WeatherHourly>>
        get() = _weatherHourlyLiveData

    private lateinit var weatherHourlyResponse: WeatherHourly


    private fun findCityWeather(cityName: String) {
        _weatherLiveData.postValue(State.loading())
        viewModelScope.launch(Dispatchers.IO) {
            try {
                weatherResponse =
                    repository.findCityWeather(cityName)
                Log.d("testLog", weatherResponse.toString())
                addWeatherDetailIntoDb(weatherResponse)
                withContext(Dispatchers.Main) {
                    val weatherDetail = WeatherDetail()
                    convertWeatherResponseToWeatherDetail(weatherDetail)
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

    private fun convertWeatherResponseToWeatherDetail(weatherDetail: WeatherDetail) {
        //fixMe
        weatherDetail.description = weatherResponse.weather[0].description
        weatherDetail.icon = weatherResponse.weather.first().icon
        weatherDetail.cityName = weatherResponse.name
        weatherDetail.countryName = weatherResponse.sys.country
        weatherDetail.temp = weatherResponse.main.temp
    }

    fun fetchWeatherDetailFromDb(cityName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val weatherDetail = repository.fetchWeatherDetail(cityName.toLowerCase())
            withContext(Dispatchers.Main) {
                if (weatherDetail != null) {
                    //AppUtils.isTimeExpired(weatherDetail.dateTime)
                    if (true) {
                        // если устарело время
                        //делаем запрос
                        findCityWeather(cityName)
                    } else {
                        // если вермя еще норм
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

    fun fetchWeatherHourly() {
        _weatherHourlyLiveData.postValue(State.loading())
        viewModelScope.launch(Dispatchers.IO) {
            _weatherHourlyLiveData.postValue(
                    State.success(
                        repository.getCityForecastHourly("55.75222","37.61556")
                    )
                )
        }
    }

        private suspend fun addWeatherDetailIntoDb(weatherResponse: WeatherDataResponse) {
            val weatherDetail = WeatherDetail()
            //fixMe
            weatherDetail.description = weatherResponse.weather[0].description
            weatherDetail.id = weatherResponse.id
            weatherDetail.icon = weatherResponse.weather.first().icon
            weatherDetail.cityName = weatherResponse.name.toLowerCase()
            weatherDetail.countryName = weatherResponse.sys.country
            weatherDetail.temp = weatherResponse.main.temp
//        weatherDetail.dateTime = AppUtils.getCurrentDateTime(AppConstants.DATE_FORMAT_1)
            repository.addWeather(weatherDetail)
        }

    fun fetchWeatherHourlyFromDb() {

    }
}