package com.example.weatherapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.data.local.entity.WeatherHourlyEntity
import com.example.weatherapp.data.model.WeatherHourlyResponse
import com.example.weatherapp.databinding.ItemForecastHourlyBinding
import com.example.weatherapp.util.convertToImageSource
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToLong

class ForecastAdapter : RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {

    private val forecastList = ArrayList<WeatherHourlyEntity>()

    class ForecastViewHolder(private val binding: ItemForecastHourlyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(weatherHourlyEntity: WeatherHourlyEntity) {
            binding.apply {
                itemTemperature.text = weatherHourlyEntity.temp?.roundToLong().toString() + "°"
                itemIcon.setImageResource(weatherHourlyEntity.icon.convertToImageSource())
                val timeConverted = weatherHourlyEntity.dt?.let { getDateTime(it) }
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

    fun setData(newHourlyList: List<WeatherHourlyEntity>) {
        forecastList.clear()
        forecastList.addAll(newHourlyList)
        notifyDataSetChanged()
    }


}