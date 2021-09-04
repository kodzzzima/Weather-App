package com.example.weatherapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.example.weatherapp.core.BindingFragment
import com.example.weatherapp.databinding.FragmentMainBinding
import com.example.weatherapp.ui.adapters.ForecastAdapter
import com.example.weatherapp.util.State
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.math.roundToInt

@AndroidEntryPoint
class MainFragment : BindingFragment<FragmentMainBinding>() {

    private val mainFragmentViewModel: MainFragmentViewModel by viewModels()

    private lateinit var forecastAdapter: ForecastAdapter

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentMainBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        observeAPICall()
    }

    private fun setupUI() {
        mainFragmentViewModel.fetchCurrentWeatherFromDb()
        mainFragmentViewModel.fetchHourlyWeatherFromDb()
        mainFragmentViewModel.fetchDailyWeatherFromDb()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        forecastAdapter = ForecastAdapter()
        val mLayoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.rvHourly.apply {
            layoutManager = mLayoutManager
            adapter = forecastAdapter
            itemAnimator = DefaultItemAnimator()
        }

    }


    private fun observeAPICall() {
        observeCurrentWeather()
        observeWeatherDaily()
        observeWeatherHourly()
    }

    private fun observeWeatherDaily() {
        mainFragmentViewModel.weatherDailyLiveData.observe(viewLifecycleOwner, { state ->
            when (state) {
                is State.Loading -> {
                }
                is State.Success -> {
                    state.data.let { weatherDaily ->
                        binding.apply {
                            todayDescription.text =
                                weatherDaily[0].description.toString()
                            todayTemperature.text =
                                "${
                                    weatherDaily[0].tempDay?.roundToInt()
                                }°/°${weatherDaily[0].tempNight?.roundToInt()}"

                            tomorrowDescription.text =
                                weatherDaily[1].description.toString()
                            tomorrowTemperature.text = "${
                                weatherDaily[1].tempDay?.roundToInt()
                            }°/°${weatherDaily[1].tempNight?.roundToInt()}"

                            afterTomorrowDescription.text =
                                weatherDaily[2].description.toString()
                            afterTomorrowTemperature.text = "${
                                weatherDaily[2].tempDay?.roundToInt()
                            }°/°${weatherDaily[2].tempNight?.roundToInt()}"
                        }
                    }
                }
                is State.Error -> {
                    Log.d("testLog", state.message + "daily")
                }
            }
        })
    }

    private fun observeWeatherHourly() {
        mainFragmentViewModel.weatherHourlyLiveData.observe(viewLifecycleOwner, { state ->
            when (state) {
                is State.Loading -> {
                    Log.d("testLog", "loading---hourly")
                }
                is State.Success -> {
                    state.data.let { weatherDetail ->
                        forecastAdapter.setData(state.data)
                        Log.d("testLog", "success--- hourly")
                        Log.d("testLog", weatherDetail.toString())
                    }

                }
                is State.Error -> {
                    Log.d("testLog", state.message + "hourly")
                }
            }
        }
        )
    }

    private fun observeCurrentWeather() {
        mainFragmentViewModel.currentWeatherLiveData.observe(viewLifecycleOwner, { state ->
            when (state) {
                is State.Loading -> {
                    Log.d("testLog", "loading---")
                }
                is State.Success -> {
                    state.data.let { weatherDetail ->
                        binding.apply {
                            textViewTemperature.text =
                                weatherDetail.temp?.roundToInt().toString() + "°"
                            feelsLikeValue.text =
                                weatherDetail.feelsLike?.roundToInt().toString() + "°"
                            textViewDescription.text = weatherDetail.description
                            pressureValue.text = weatherDetail.pressure.toString()
                            humidityValue.text = weatherDetail.humidity.toString() + "%"
                            windSpeedValue.text = weatherDetail.wind_speed.toString() + " м/c"

                        }
                        Log.d("testLog", "success---")
                        Log.d("testLog", weatherDetail.toString())
//                        val iconCode = weatherDetail.icon?.replace("n", "d")
                    }
                }
                is State.Error -> {
                    Log.d("testLog", state.message)
                }
            }
        })
    }

}


