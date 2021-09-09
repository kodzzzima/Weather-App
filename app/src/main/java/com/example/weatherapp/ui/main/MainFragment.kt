package com.example.weatherapp.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.example.weatherapp.R
import com.example.weatherapp.core.BindingFragment
import com.example.weatherapp.databinding.FragmentMainBinding
import com.example.weatherapp.ui.adapters.ForecastAdapter
import com.example.weatherapp.util.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToLong

@AndroidEntryPoint
class MainFragment : BindingFragment<FragmentMainBinding>(), EasyPermissions.PermissionCallbacks {

    companion object {
        const val PERMISSION_LOCATION_REQUEST_CODE = 1
    }

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val mainFragmentViewModel: MainFragmentViewModel by viewModels()

    private lateinit var forecastAdapter: ForecastAdapter

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentMainBinding::inflate

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        binding.btnLocation.setOnClickListener {
            if (hasLocationPermission()) {
                fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                    val geoCoder = Geocoder(requireContext())
                    val locationLat: Double = location.latitude
                    val locationLon: Double = location.longitude
                    val currentLocation = geoCoder.getFromLocation(
                        locationLat,
                        locationLon,
                        1
                    )
                    mainFragmentViewModel.saveCityNameAndCoordinates(
                        currentLocation.first().locality.toString(),
                        locationLat.toString(),
                        locationLon.toString()
                    )
                    fetchDataFromDatabases()
                }
            } else {
                requestLocationPermission()
            }
        }

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

    private fun updateUIWithApiCalls() {
        mainFragmentViewModel.getCurrentWeatherFromApi(mainFragmentViewModel.cityPrefs)
        mainFragmentViewModel.getHourlyWeatherFromApi(
            mainFragmentViewModel.latPrefs,
            mainFragmentViewModel.lonPrefs
        )
        mainFragmentViewModel.getDailyWeatherFromApi(
            mainFragmentViewModel.latPrefs,
            mainFragmentViewModel.lonPrefs
        )
    }

    private fun fetchDataFromDatabases() {
        mainFragmentViewModel.fetchCurrentWeatherFromDb(
            mainFragmentViewModel.cityPrefs
        )
        mainFragmentViewModel.fetchHourlyWeatherFromDb(
            mainFragmentViewModel.latPrefs,
            mainFragmentViewModel.lonPrefs
        )
        mainFragmentViewModel.fetchDailyWeatherFromDb(
            mainFragmentViewModel.latPrefs,
            mainFragmentViewModel.lonPrefs
        )
    }

    private fun setupUI() {
        initRecyclerView()
        fetchDataFromDatabases()

        binding.textViewToolbarTitle.text = mainFragmentViewModel.cityPrefs

        binding.btnMore.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_weatherWeekFragment)
        }

        binding.inputFindCityWeather.setOnEditorActionListener { textView, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE) {
                if (textView.text.isNotEmpty()) {
                    mainFragmentViewModel.fetchCurrentWeatherFromDb((textView as EditText).text.toString())
                }
                binding.apply {
                    inputFindCityWeather.text?.clear()
                    textViewToolbarTitle.visibility = View.VISIBLE
                    btnSearch.visibility = View.VISIBLE
                    btnLocation.visibility = View.VISIBLE
                    inputFindCityWeather.visibility = View.GONE
                }
            }
            false
        }

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

        observeLatLonChanges()
    }

    private fun observeLatLonChanges() {
        mainFragmentViewModel.latAndLonLiveData.observe(viewLifecycleOwner, {
            val lat = it[0]
            val lon = it[1]
            mainFragmentViewModel.getDailyWeatherFromApi(lat, lon)
            mainFragmentViewModel.getHourlyWeatherFromApi(lat, lon)
        })
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
                            todayTempDay.text =
                                weatherDaily[0].tempDay?.roundTemperatureAndGetString()
                            todayTempNight.text =
                                weatherDaily[0].tempNight?.roundTemperatureAndGetString()
                            todayIcon.setImageResource(weatherDaily[0].icon.convertToImageSource())

                            tomorrowDescription.text =
                                weatherDaily[1].description?.titleCaseFirstChar()
                            tomorrowTempDay.text =
                                weatherDaily[1].tempDay?.roundTemperatureAndGetString()
                            tomorrowTempNight.text =
                                weatherDaily[1].tempNight?.roundTemperatureAndGetString()
                            tomorrowIcon.setImageResource(weatherDaily[1].icon.convertToImageSource())

                            afterTomorrowDescription.text =
                                weatherDaily[2].description?.titleCaseFirstChar()
                            afterTomorrowTempDay.text =
                                weatherDaily[2].tempDay?.roundTemperatureAndGetString()
                            afterTomorrowTempNight.text =
                                weatherDaily[2].tempNight?.roundTemperatureAndGetString()
                            afterTomorrowIcon.setImageResource(weatherDaily[2].icon.convertToImageSource())
                        }
                    }
                }
                is State.Error -> {
                    Log.d("testLog", state.message)
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun observeWeatherHourly() {
        mainFragmentViewModel.weatherHourlyLiveData.observe(viewLifecycleOwner, { state ->
            when (state) {
                is State.Loading -> {
                }
                is State.Success -> {
                    state.data.let { weatherHourly ->
                        forecastAdapter.setData(weatherHourly)
                    }
                }
                is State.Error -> {
                    Log.d("testLog", state.message)
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
        )
    }

    @SuppressLint("SetTextI18n")
    private fun observeCurrentWeather() {
        mainFragmentViewModel.currentWeatherLiveData.observe(viewLifecycleOwner, { state ->
            when (state) {
                is State.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.textViewTemperature.visibility = View.GONE
                    binding.textViewDescription.visibility = View.GONE
                }
                is State.Success -> {

                    state.data.let { weatherDetail ->
                        binding.apply {
                            binding.progressBar.visibility = View.GONE
                            binding.textViewTemperature.visibility = View.VISIBLE
                            binding.textViewDescription.visibility = View.VISIBLE

                            textViewToolbarTitle.text =
                                weatherDetail.cityName

                            textViewTemperature.text =
                                weatherDetail.temp?.roundTemperatureAndGetString()

                            feelsLikeValue.text =
                                weatherDetail.feelsLike?.roundTemperatureAndGetString()

                            textViewDescription.text =
                                weatherDetail.description?.titleCaseFirstChar()

                            pressureValue.text =
                                weatherDetail.pressure.pressureConvertToMM()

                            humidityValue.text =
                                weatherDetail.humidity.toString() + "%"

                            windSpeedValue.text =
                                weatherDetail.wind_speed?.roundToLong().toString() + " м/c"
                        }
                    }
                }
                is State.Error -> {
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun hasLocationPermission() =
        EasyPermissions.hasPermissions(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )

    private fun requestLocationPermission() {
        EasyPermissions.requestPermissions(
            this,
            "Для определения погоды в вашем городе мне нужен доступ к вашей геопозиции.",
            PERMISSION_LOCATION_REQUEST_CODE,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms.first())) {
            SettingsDialog.Builder(requireActivity()).build().show()
        } else {
            requestLocationPermission()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {}

}


