package com.example.weatherapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.data.model.WeatherDetail
import com.example.weatherapp.databinding.ItemForecastHourlyBinding

class ForecastAdapter : RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {

    private val weatherList = ArrayList<WeatherDetail>()

    class ForecastViewHolder(private val binding: ItemForecastHourlyBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(weatherDetail: WeatherDetail){
            binding.apply{

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
        holder.bind(weatherList[position])
    }

    override fun getItemCount(): Int = weatherList.size

    fun updateData(){

    }
}