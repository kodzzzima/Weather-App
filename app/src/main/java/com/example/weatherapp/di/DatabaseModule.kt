package com.example.weatherapp.di

import android.content.Context
import androidx.room.Room
import com.example.weatherapp.data.local.dao.WeatherHourlyDao
import com.example.weatherapp.data.local.WeatherDatabase
import com.example.weatherapp.data.local.dao.WeatherDailyDao
import com.example.weatherapp.data.local.dao.WeatherCurrentDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): WeatherDatabase =
        Room.databaseBuilder(
            context,
            WeatherDatabase::class.java,
            "WeatherApp-DB"
        ).build()

    @Provides
    @Singleton
    fun provideCurrentWeatherDao(db: WeatherDatabase): WeatherCurrentDao = db.getWeatherDao()

    @Provides
    @Singleton
    fun provideHourlyForecastDao(db: WeatherDatabase): WeatherHourlyDao = db.getWeatherHourlyDao()

    @Provides
    @Singleton
    fun provideDailyForecastDao(db: WeatherDatabase): WeatherDailyDao = db.getWeatherDailyDao()

}