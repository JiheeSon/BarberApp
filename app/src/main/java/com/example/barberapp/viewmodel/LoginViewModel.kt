package com.example.barberapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.barberapp.model.Repository
import com.example.barberapp.model.remote.request.LoginRequest
import com.example.barberapp.model.remote.response.LoginResponse

class LoginViewModel(private val repository: Repository): ViewModel() {
    val mobileNo = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val fcmToken = MutableLiveData<String>()

    val loginResponse: LiveData<LoginResponse> = repository.loginResponse
    val error: LiveData<String> = repository.error
    val isProcessing = repository.isProcessing

    fun onLoginClick() {
        Log.i("jihee", "hello")
        val loginRequest = LoginRequest(mobileNo.value!!, password.value!!)
        repository.login(loginRequest, "7XYp53TWyiAdxl8CtERFJMqcOD1wbNnG")
    }
}