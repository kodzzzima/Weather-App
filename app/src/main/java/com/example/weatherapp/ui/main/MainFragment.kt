package com.example.weatherapp.ui.main

import android.os.Bundle
import android.util.Log
import android.view.*
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
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt


@AndroidEntryPoint
class MainFragment : BindingFragment<FragmentMainBinding>() {

    private val mainFragmentViewModel: MainFragmentViewModel by viewModels()

    private lateinit var forecastAdapter: ForecastAdapter

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentMainBinding::inflate


    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity?)!!.setSupportActionBar(binding.toolbar)
        setupUI()
        observeAPICall()
    }

    private fun setupUI() {
        mainFragmentViewModel.fetchCurrentWeatherFromDb()
        mainFragmentViewModel.fetchHourlyWeatherFromDb()
        mainFragmentViewModel.fetchDailyWeatherFromDb()
        binding.btnMore.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_weatherWeekFragment)
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
                                weatherDaily[0].description.toString()
                            todayTemperature.text =
                                "${
                                    weatherDaily[0].tempDay?.roundToInt()
                                }°/°${weatherDaily[0].tempNight?.roundToInt()}"

                            todayIcon.setImageResource(weatherDaily[0].icon.convertToImageSource())

                            tomorrowDescription.text =
                                weatherDaily[1].description.toString()
                            tomorrowTemperature.text = "${
                                weatherDaily[1].tempDay?.roundToInt()
                            }°/°${weatherDaily[1].tempNight?.roundToInt()}"
                            tomorrowIcon.setImageResource(weatherDaily[1].icon.convertToImageSource())

                            afterTomorrowDescription.text =
                                weatherDaily[2].description.toString()
                            afterTomorrowTemperature.text = "${
                                weatherDaily[2].tempDay?.roundToInt()
                            }°/°${weatherDaily[2].tempNight?.roundToInt()}"
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
                        for (item in state.data) {
                            Log.d("testLog", item.icon.toString())
                        }
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


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)

        val search = menu.findItem(R.id.menuItemSearch)
        val searchView = search?.actionView as? SearchView
        searchView?.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    Log.d("testLog", "daaaa")
                    Toast.makeText(requireContext(), "da", Toast.LENGTH_SHORT).show()
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }

            }
        )
        super.onCreateOptionsMenu(menu, inflater)
    }

//    fun covertImgCodeToSource(code: String?): Int {
//        var codeInteger = 2131165304
//        when (code) {
//            "01d", "01n" -> codeInteger = R.drawable.ic_clear_sky
//            "02d", "02n" -> codeInteger = R.drawable.ic_few_clouds
//            "03d", "03n" -> codeInteger = R.drawable.ic_scattered_clouds
//            "04n", "04d" -> codeInteger = R.drawable.ic_broken_clouds
//            "09d" -> codeInteger = R.drawable.ic_shower_rain
//            "50d" -> codeInteger = R.drawable.ic_mist
//            "10d" -> codeInteger = R.drawable.ic_rain
//            "11d" -> codeInteger = R.drawable.ic_thunderstorm
//            "13d" -> codeInteger = R.drawable.ic_snow
//        }
//        return codeInteger
//    }

}


