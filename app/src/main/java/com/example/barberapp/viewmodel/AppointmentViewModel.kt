package com.example.barberapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.barberapp.model.Repository

class AppointmentViewModel(private val repository: Repository): ViewModel() {
    val barbersResponse = repository.barbersResponse
    val error: LiveData<String> = repository.error
    val isProcessing = repository.isProcessing

    val selectedBarberId = MutableLiveData<Int>()

    fun getBarberList() {
        repository.getBarbers()
    }
}