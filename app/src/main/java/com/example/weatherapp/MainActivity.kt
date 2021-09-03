package com.example.weatherapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.weatherapp.data.ApiException
import com.example.weatherapp.data.NoInternetException
import com.example.weatherapp.data.local.entity.WeatherDetailEntity
import com.example.weatherapp.data.model.WeatherDataResponse
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.util.Constants
import com.example.weatherapp.util.State
import com.example.weatherapp.util.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private lateinit var binding: ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menuItemSearch -> {
                findNavController(R.id.container_fragment).navigate(R.id.searchFragment)
                true
            }
            else -> false
        }
    }
}

