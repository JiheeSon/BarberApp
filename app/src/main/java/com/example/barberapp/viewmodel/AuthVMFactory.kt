package com.example.barberapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.barberapp.model.Repository
import java.lang.IllegalArgumentException

class AuthVMFactory constructor(val repository: Repository): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            AuthViewModel(this.repository) as T
        }
        else
        {
            throw IllegalArgumentException("View Model not found!!")
        }
    }
}