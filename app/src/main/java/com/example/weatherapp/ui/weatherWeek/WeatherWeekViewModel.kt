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
import com.example.weatherapp.util.toEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class WeatherWeekViewModel @Inject internal constructor(
    private val weatherDailyRepository: WeatherDailyRepository,
    private var sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _weatherDailyLiveData =
        MutableLiveData<State<List<WeatherDailyEntity>>>()
    val weatherDailyLiveData: LiveData<State<List<WeatherDailyEntity>>>
        get() = _weatherDailyLiveData

    val latPrefs: String? =
        sharedPreferences.getString(Constants.Preferences.LAT, Constants.Preferences.LAT_DEFAULT)
    val lonPrefs: String? =
        sharedPreferences.getString(Constants.Preferences.LON, Constants.Preferences.LON_DEFAULT)

    fun getDailyWeatherFromApi(lat: String?, lon: String?) {
        var weatherDailyResponse: WeatherDailyResponse? = null
        _weatherDailyLiveData.postValue(State.loading())
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (lat?.isNotEmpty() == true && lon?.isNotEmpty() == true) {
                    weatherDailyResponse =
                        weatherDailyRepository.getDailyWeatherFromApi(lat, lon)
                }
                withContext(Dispatchers.Main) {
                    val weatherDailyToRV = weatherDailyResponse?.toEntity()
                    _weatherDailyLiveData.postValue(
                        State.success(
                            weatherDailyToRV ?: emptyList()
                        )
                    )
                }
            } catch (e: ApiException) {
                withContext(Dispatchers.Main) {
                    _weatherDailyLiveData.postValue(State.error(e.message.orEmpty()))
                }
            } catch (e: NoInternetException) {
                withContext(Dispatchers.Main) {
                    _weatherDailyLiveData.postValue(State.error(e.message.orEmpty()))
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _weatherDailyLiveData.postValue(
                        State.error(
                            e.message.orEmpty()
                        )
                    )
                }
            }
        }
    }
}