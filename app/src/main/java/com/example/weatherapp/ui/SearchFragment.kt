package com.example.weatherapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.viewbinding.ViewBinding
import com.example.weatherapp.R
import com.example.weatherapp.core.BindingFragment
import com.example.weatherapp.databinding.FragmentSearchBinding
import com.example.weatherapp.databinding.FragmentWeatherWeekBinding
import dagger.hilt.android.AndroidEntryPoint
//
//@AndroidEntryPoint
//class SearchFragment : BindingFragment<FragmentSearchBinding>() {
//
//    override val bindingInflater: (LayoutInflater) -> ViewBinding
//        get() = FragmentSearchBinding::inflate
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
////        initSearchView()
//        initRecyclerView()
//    }
//
//    private fun initRecyclerView() {
//        TODO("Not yet implemented")
//    }
//
////    private fun initSearchView() {
////        binding.searchView.setOnQueryTextListener(
////            object : SearchView.OnQueryTextListener{
////                override fun onQueryTextSubmit(p0: String?): Boolean {
////                    TODO("Not yet implemented")
////                }
////
////                override fun onQueryTextChange(p0: String?): Boolean {
////                    TODO("Not yet implemented")
////                }
////
////            }
////        )
////    }
//}