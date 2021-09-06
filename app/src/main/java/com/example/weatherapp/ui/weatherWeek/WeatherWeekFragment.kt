package com.example.weatherapp.ui.weatherWeek

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.example.weatherapp.R
import com.example.weatherapp.core.BindingFragment
import com.example.weatherapp.databinding.FragmentWeatherWeekBinding
import com.example.weatherapp.ui.adapters.WeatherWeekAdapter
import com.example.weatherapp.util.State
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherWeekFragment : BindingFragment<FragmentWeatherWeekBinding>() {

    private val weatherWeekViewModel: WeatherWeekViewModel by viewModels()

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentWeatherWeekBinding::inflate

    private lateinit var weatherWeekAdapter: WeatherWeekAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        observeAPICall()
    }

    private fun observeAPICall() {
        weatherWeekViewModel.weatherDailyLiveData.observe(viewLifecycleOwner, { state ->
            when (state) {
                is State.Loading -> {
                }
                is State.Success -> {
                    state.data.let { weatherDaily ->
                        weatherWeekAdapter.setData(state.data)
//                            todayDescription.text =
//                                weatherDaily[0].description.toString()
//                            todayTemperature.text =
//                                "${
//                                    weatherDaily[0].tempDay?.roundToInt()
//                                }°/°${weatherDaily[0].tempNight?.roundToInt()}"
//
//                            tomorrowDescription.text =
//                                weatherDaily[1].description.toString()
//                            tomorrowTemperature.text = "${
//                                weatherDaily[1].tempDay?.roundToInt()
//                            }°/°${weatherDaily[1].tempNight?.roundToInt()}"
//
//                            afterTomorrowDescription.text =
//                                weatherDaily[2].description.toString()
//                            afterTomorrowTemperature.text = "${
//                                weatherDaily[2].tempDay?.roundToInt()
//                            }°/°${weatherDaily[2].tempNight?.roundToInt()}"

                    }
                }
                is State.Error -> {
                    Log.d("testLog", state.message + "daily")
                }
            }
        })
    }

    private fun setupUI() {
        initRecyclerView()
        weatherWeekViewModel.fetchDailyWeatherFromDb()
        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_weatherWeekFragment_to_mainFragment)
        }
    }

    private fun initRecyclerView() {
        weatherWeekAdapter = WeatherWeekAdapter()
        val mLayoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.rvWeek.apply {
            layoutManager = mLayoutManager
            adapter = weatherWeekAdapter
            itemAnimator = DefaultItemAnimator()
        }
    }


}