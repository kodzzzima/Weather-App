package com.example.weatherapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.data.local.entity.WeatherDailyEntity
import com.example.weatherapp.databinding.ItemWeatherWeekBinding
import com.example.weatherapp.util.*

class WeatherWeekAdapter : RecyclerView.Adapter<WeatherWeekAdapter.WeatherWeekViewHolder>() {

    private val weatherWeekList = ArrayList<WeatherDailyEntity>()

    class WeatherWeekViewHolder(private val binding: ItemWeatherWeekBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(weatherDailyEntity: WeatherDailyEntity) {
            binding.apply {
                txtDayOfWeek.text = weatherDailyEntity.dt.getDayOfWeek()
                txtDayNumber.text = weatherDailyEntity.dt.getDayAndMonth()
                txtTempDay.text = weatherDailyEntity.tempDay?.roundTemperatureAndGetString()
                txtTempNight.text = weatherDailyEntity.tempNight?.roundTemperatureAndGetString()
                txtDescription.text = weatherDailyEntity.description?.titleCaseFirstChar()
                imgItem.setImageResource(weatherDailyEntity.icon.convertToImageSource())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherWeekViewHolder {
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
}