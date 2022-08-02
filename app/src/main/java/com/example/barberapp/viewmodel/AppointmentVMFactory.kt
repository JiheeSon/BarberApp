package com.example.barberapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.barberapp.model.Repository

class AppointmentVMFactory(val repository: Repository): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AppointmentViewModel(repository) as T
    }
}