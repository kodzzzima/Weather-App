package com.example.weatherapp.ui.weatherWeek

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
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

    private val weatherWeekAdapter by lazy { WeatherWeekAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        observeAPICall()
    }

    private fun observeAPICall() {
        weatherWeekViewModel.weatherDailyLiveData.observe(viewLifecycleOwner, { state ->
            when (state) {
                is State.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.cardviewRecycler.visibility = View.GONE
                }
                is State.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.cardviewRecycler.visibility = View.VISIBLE
                    state.data.let { weatherDaily ->
                        weatherWeekAdapter.setData(weatherDaily)
                    }
                }
                is State.Error -> {
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun setupUI() {
        initRecyclerView()

        weatherWeekViewModel.getDailyWeatherFromApi(
            weatherWeekViewModel.latPrefs,
            weatherWeekViewModel.lonPrefs
        )

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun initRecyclerView() {
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