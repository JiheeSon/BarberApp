package com.example.barberapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.barberapp.model.Repository
import com.example.barberapp.model.remote.ApiService
import com.example.barberapp.model.remote.request.RegistrationRequest
import com.example.barberapp.model.remote.response.RegistrationResponse
import com.example.barberapp.model.util.confirmPassword
import com.example.barberapp.model.util.isMobileValid
import com.example.barberapp.model.util.isNotEmpty

class RegistrationViewModel(private val repository: Repository) : ViewModel() {
    val mobileNo = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val passwordConfirm = MutableLiveData<String>()
    val fcmToken = MutableLiveData<String>()

    val isMobileNotEmpty = MutableLiveData<Boolean>()
    val isPasswordNotEmpty = MutableLiveData<Boolean>()
    val isConfirmNotEmpty = MutableLiveData<Boolean>()
    val isMobileValid = MutableLiveData<Boolean>()
    val matchPassword = MutableLiveData<Boolean>()

    val registrationResponse: LiveData<RegistrationResponse> = repository.registrationResponse
    val error: LiveData<String> = repository.error
    val isProcessing = repository.isProcessing

    fun onRegisterClick() {
        if (validateInput()) {
            val registrationRequest = RegistrationRequest(fcmToken.value!!, mobileNo.value!!, password.value!!)
            repository.register(registrationRequest)
        }
    }

    private fun validateInput(): Boolean {
        isMobileNotEmpty.value = isNotEmpty(mobileNo.value)
        isPasswordNotEmpty.value = isNotEmpty(password.value)
        isConfirmNotEmpty.value = isNotEmpty(passwordConfirm.value)

        if (!(isMobileNotEmpty.value!! && isConfirmNotEmpty.value!! && isPasswordNotEmpty.value!!)) {
            return false
        }

        isMobileValid.value = isMobileValid(mobileNo.value.toString())
        matchPassword.value = confirmPassword(password.value.toString(), passwordConfirm.value.toString())

        return isMobileValid.value!! &&
                isConfirmNotEmpty.value!! &&
                isMobileNotEmpty.value!! &&
                isPasswordNotEmpty.value!! &&
                matchPassword.value!!
    }
}