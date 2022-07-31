package com.example.barberapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.barberapp.model.Repository

class ServiceViewModel(private val repository: Repository): ViewModel() {
    val serviceResponse = repository.serviceResponse
    val error: LiveData<String> = repository.error
    val isProcessing = repository.isProcessing


    fun getService() {
        repository.getService()
    }
}