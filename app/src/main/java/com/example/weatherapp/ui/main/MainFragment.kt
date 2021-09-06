package com.example.weatherapp.ui.main

import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.example.weatherapp.MainActivity
import com.example.weatherapp.R
import com.example.weatherapp.core.BindingFragment
import com.example.weatherapp.databinding.FragmentMainBinding
import com.example.weatherapp.ui.adapters.ForecastAdapter
import com.example.weatherapp.util.State
import com.example.weatherapp.util.convertToImageSource
import com.example.weatherapp.util.roundTemperatureAndGetString
import com.example.weatherapp.util.titleCaseFirstChar
import dagger.hilt.android.AndroidEntryPoint
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
        binding.apply {
            btnSearch.setOnClickListener {
                textViewToolbarTitle.visibility = View.GONE
                btnSearch.visibility = View.GONE
                btnLocation.visibility = View.GONE
                inputFindCityWeather.visibility = View.VISIBLE
            }
        }
    }

    private fun setupUI() {
        mainFragmentViewModel.cityPrefs?.let { mainFragmentViewModel.fetchCurrentWeatherFromDb(it) }
        mainFragmentViewModel.fetchHourlyWeatherFromDb()
        mainFragmentViewModel.fetchDailyWeatherFromDb()
        binding.btnMore.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_weatherWeekFragment)
        }
        binding.inputFindCityWeather.setOnEditorActionListener { textView, id, keyEvent ->
            if (id == EditorInfo.IME_ACTION_DONE) {
                mainFragmentViewModel.fetchCurrentWeatherFromDb((textView as EditText).text.toString())
                binding.apply {
                    textViewToolbarTitle.visibility = View.VISIBLE
                    btnSearch.visibility = View.VISIBLE
                    btnLocation.visibility = View.VISIBLE
                    inputFindCityWeather.visibility = View.GONE
                }
            }
            false
        }
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
                                weatherDaily[0].description?.titleCaseFirstChar()
                            todayTempDay.text = weatherDaily[0].tempDay?.roundTemperatureAndGetString()
                            todayTempNight.text = weatherDaily[0].tempNight?.roundTemperatureAndGetString()
                            todayIcon.setImageResource(weatherDaily[0].icon.convertToImageSource())

                            tomorrowDescription.text =
                                weatherDaily[1].description?.titleCaseFirstChar()
                            tomorrowTempDay.text = weatherDaily[1].tempDay?.roundTemperatureAndGetString()
                            tomorrowTempNight.text = weatherDaily[1].tempNight?.roundTemperatureAndGetString()
                            tomorrowIcon.setImageResource(weatherDaily[1].icon.convertToImageSource())

                            afterTomorrowDescription.text =
                                weatherDaily[2].description?.titleCaseFirstChar()
                            afterTomorrowTempDay.text = weatherDaily[2].tempDay?.roundTemperatureAndGetString()
                            afterTomorrowTempNight.text = weatherDaily[2].tempNight?.roundTemperatureAndGetString()
                            afterTomorrowIcon.setImageResource(weatherDaily[2].icon.convertToImageSource())
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
                        forecastAdapter.setData(weatherDetail)
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
                }
                is State.Success -> {
                    state.data.let { weatherDetail ->
                        binding.apply {
                            textViewTemperature.text =
                                weatherDetail.temp?.roundTemperatureAndGetString()
                            feelsLikeValue.text =
                                weatherDetail.feelsLike?.roundTemperatureAndGetString()
                            textViewDescription.text = weatherDetail.description?.titleCaseFirstChar()
                            pressureValue.text = weatherDetail.pressure.toString()
                            humidityValue.text = weatherDetail.humidity.toString() + "%"
                            windSpeedValue.text = weatherDetail.wind_speed.toString() + " м/c"

                        }
                    }
                }
                is State.Error -> {
                }
            }
        })
    }
}


