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
import java.time.Instant
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
        mainFragmentViewModel.fetchWeatherDetailFromDb("Moscow")
        mainFragmentViewModel.fetchWeatherHourlyFromDb()
        initRecyclerView()
    }

    private fun initRecyclerView() {
//        forecastAdapter = ForecastAdapter()
//        val mLayoutManager = LinearLayoutManager(
//            context,
//            LinearLayoutManager.HORIZONTAL,
//            false
//        )
//        binding.rvHourly.apply {
//            layoutManager = mLayoutManager
//            adapter = forecastAdapter
//            itemAnimator = DefaultItemAnimator()
//        }

    }


    private fun observeAPICall() {

//        val dt = Instant.ofEpochSecond(1630508400).atZone()
        mainFragmentViewModel.weatherHourlyLiveData.observe(viewLifecycleOwner, { state ->

            when (state) {
                is State.Loading -> {
                    Log.d("testLog", "loading---hourly")
                }
                is State.Success -> {
                    state.data.let { weatherDetail ->
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

        mainFragmentViewModel.weatherLiveData.observe(viewLifecycleOwner, { state ->
            when (state) {
                is State.Loading -> {
                    Log.d("testLog", "loading---")
                }
                is State.Success -> {
                    state.data.let { weatherDetail ->
                        binding.textViewTemperature.text =
                            weatherDetail.temp?.roundToInt().toString() + "°"
                        binding.textViewDescription.text = weatherDetail.description
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

    private fun getDateTimeFromEpocLongOfSeconds(epoc: Long): String? {
        return try {
            val netDate = Date(epoc * 1000)
            netDate.toString()
        } catch (e: Exception) {
            e.toString()
        }
    }

}


