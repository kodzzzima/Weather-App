package com.example.weatherapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.data.model.WeatherHourlyResponse
import com.example.weatherapp.databinding.ItemForecastHourlyBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToLong

class ForecastAdapter : RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {

    private val forecastList = ArrayList<WeatherHourlyResponse.Hourly>()

    class ForecastViewHolder(private val binding: ItemForecastHourlyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(hourlyResponse: WeatherHourlyResponse.Hourly) {
            binding.apply {
                itemTemperature.text = hourlyResponse.temp.roundToLong().toString() + "°"
                val timeConverted = getDateTime(hourlyResponse.dt)
                itemTime.text = timeConverted
            }
        }
        private fun getDateTime(s: Int): String? {
            try {
                val sdf = SimpleDateFormat("HH:mm")
                val netDate = Date(s.toLong()*1000)
                return sdf.format(netDate)
            } catch (e: Exception) {
                return e.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val binding = ItemForecastHourlyBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ForecastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        holder.bind(forecastList[position])
    }

    override fun getItemCount(): Int = forecastList.size

    fun setData(newHourlyList: List<WeatherHourlyResponse.Hourly>) {
        forecastList.clear()
        forecastList.addAll(newHourlyList)
        notifyDataSetChanged()
    }


}