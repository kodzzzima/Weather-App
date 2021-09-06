package com.example.weatherapp.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.data.local.entity.WeatherDailyEntity
import com.example.weatherapp.databinding.ItemWeatherWeekBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToLong

class WeatherWeekAdapter:RecyclerView.Adapter<WeatherWeekAdapter.WeatherWeekViewHolder>() {

    private val weatherWeekList = ArrayList<WeatherDailyEntity>()

    class WeatherWeekViewHolder(private val binding: ItemWeatherWeekBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(weatherDailyEntity: WeatherDailyEntity) {
            binding.apply {
                txtTempDay.text = weatherDailyEntity.tempDay?.roundToLong().toString() + "° / "
                txtTempNight.text = weatherDailyEntity.tempNight?.roundToLong().toString() + "°"
                txtDescription.text = weatherDailyEntity.description
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherWeekViewHolder{
        val binding = ItemWeatherWeekBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return WeatherWeekViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherWeekViewHolder, position: Int) {
        holder.bind(weatherWeekList[position])
    }

    override fun getItemCount(): Int = weatherWeekList.size

    fun setData(newDailyList: List<WeatherDailyEntity>) {
        weatherWeekList.clear()
        weatherWeekList.addAll(newDailyList)
        notifyDataSetChanged()
    }

    private fun convert
}