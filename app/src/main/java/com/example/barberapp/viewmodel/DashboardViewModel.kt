package com.example.barberapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.barberapp.model.Repository
import com.example.barberapp.model.remote.response.DashboardResponse
import com.example.barberapp.model.remote.response.LoginResponse

class DashboardViewModel(private val repository: Repository): ViewModel() {

    val error: LiveData<String> = repository.error
    val isProcessing = repository.isProcessing
    val dashboardResponse: LiveData<DashboardResponse> = repository.dashboardResponse

    fun getDashboard() {
        repository.getDashboard()
    }

    fun preloadData() {
        //hours
        //notification

        //barber
        //hot deal
    }
}