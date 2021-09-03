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
import com.example.weatherapp.util.Constants
import com.example.weatherapp.util.State
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
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
        val lat: String? =
            mainFragmentViewModel.sharedPreferences.getString(Constants.Coords.LAT, "")
        Log.d("testLog", "lat = " + lat)
    }

    private fun setupUI() {
        mainFragmentViewModel.fetchWeatherDetailFromDb("Moscow")
        mainFragmentViewModel.fetchHourlyWeatherFromDb()
        mainFragmentViewModel.fetchWeatherDailyFromDb()
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
        mainFragmentViewModel.weatherHourlyLiveData.observe(viewLifecycleOwner, { state ->

            when (state) {
                is State.Loading -> {
                    Log.d("testLog", "loading---hourly")
                }
                is State.Success -> {
                    state.data.let { weatherDetail ->
                        forecastAdapter.setData(state.data.hourly)
                        Log.d("testLog", "success--- hourly")
                        Log.d("testLog", weatherDetail.toString())
//                        val iconCode = weatherDetail.icon?.replace("n", "d")
                    }

                }
                is State.Error -> {
                    Log.d("testLog", state.message + "hourly")
                }
            }
        }
        )

        mainFragmentViewModel.weatherDailyLiveData.observe(viewLifecycleOwner,{ state ->
            when (state) {
                is State.Loading -> {
                    Log.d("testLog", "loading---daily")
                }
                is State.Success -> {
                    state.data.let { weatherDaily ->
                        Log.d("testLog", "success--- daily")
                        Log.d("testLog", weatherDaily[0].description.toString())
                    }

                }
                is State.Error -> {
                    Log.d("testLog", state.message + "daily")
                }
            }
        })


        mainFragmentViewModel.weatherLiveData.observe(viewLifecycleOwner, { state ->
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

    private fun getDateTime(s: Int): String? {
        try {
            val sdf = SimpleDateFormat("HH:mm")
            val netDate = Date(s.toLong() * 1000)
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }

}


