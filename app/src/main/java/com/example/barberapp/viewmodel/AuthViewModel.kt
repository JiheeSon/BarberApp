package com.example.barberapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.barberapp.model.Repository
import com.example.barberapp.model.remote.request.LoginRequest
import com.example.barberapp.model.remote.request.RegistrationRequest
import com.example.barberapp.model.remote.response.LoginResponse
import com.example.barberapp.model.remote.response.RegistrationResponse
import com.example.barberapp.model.util.confirmPassword
import com.example.barberapp.model.util.isMobileValid
import com.example.barberapp.model.util.isNotEmpty
import com.example.barberapp.model.util.isPasswordValid
import com.google.gson.Gson
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AuthViewModel(private val repository: Repository): ViewModel() {
    var compositeDisposable: CompositeDisposable = CompositeDisposable()

    val mobileNo = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val passwordConfirm = MutableLiveData<String>()
    val fcmToken = MutableLiveData<String>()

    val mobileError = MutableLiveData<String>()
    val passwordError = MutableLiveData<String>()
    val confirmPasswordError = MutableLiveData<String>()

    val registrationResponse: LiveData<RegistrationResponse> = repository.registrationResponse
    val loginResponse: LiveData<LoginResponse> = repository.loginResponse
    val error: LiveData<String> = repository.error
    val isProcessing = repository.isProcessing

    fun onRegisterClick() {
        if (validateRegistrationInput()) {
            val registrationRequest = RegistrationRequest(fcmToken.value!!, mobileNo.value!!, password.value!!)
            repository.register(registrationRequest)
        }
    }

    fun onLoginClick() {
        if (validateLoginInput()) {
            val loginRequest = LoginRequest(mobileNo.value!!, password.value!!)
            repository.login(loginRequest)
        }
    }

    private fun validateRegistrationInput(): Boolean {
        mobileError.value = if (isNotEmpty(mobileNo.value)) null else "Required"
        passwordError.value = if (isNotEmpty(password.value)) null else "Required"
        confirmPasswordError.value = if (isNotEmpty(passwordConfirm.value)) null else "Required"
        if (!(isNotEmpty(mobileNo.value) && isNotEmpty(password.value) && isNotEmpty(passwordConfirm.value))) {
            return false
        }

        mobileError.value = if (isMobileValid(mobileNo.value.toString())) null else "Invalid Number"
        passwordError.value = if (isPasswordValid(password.value.toString())) null else "Password should be at least 8 characters"
        confirmPasswordError.value = if (confirmPassword(password.value.toString(), passwordConfirm.value.toString())) null else "Please check again"

        return isNotEmpty(mobileNo.value) &&
                isNotEmpty(password.value) &&
                isNotEmpty(passwordConfirm.value) &&
                isMobileValid(mobileNo.value.toString()) &&
                isPasswordValid(password.value.toString()) &&
                confirmPassword(password.value.toString(), passwordConfirm.value.toString())
    }

    private fun validateLoginInput(): Boolean {
        mobileError.value = if (isNotEmpty(mobileNo.value)) null else "Required"
        passwordError.value = if (isNotEmpty(password.value)) null else "Required"

        if (!(isNotEmpty(mobileNo.value) && isNotEmpty(password.value))) {
            return false
        }

        mobileError.value = if (isMobileValid(mobileNo.value.toString())) null else "Invalid Number"
        passwordError.value = if (isPasswordValid(password.value.toString())) null else "Password should be at least 8 characters"

        return isNotEmpty(mobileNo.value) &&
                isNotEmpty(password.value) &&
                isMobileValid(mobileNo.value.toString()) &&
                isPasswordValid(password.value.toString())
    }

    fun updateFcmToken (fcmToken: String) {
        val ps_auth_token = loginResponse.value!!.apiToken
        val map = HashMap<String, Any>()
        map["userId"] = loginResponse.value!!.userId
        map["fcmToken"] = fcmToken
        map["application"] = "BarberApp"
        val reqJson: String = Gson().toJson(map)
        val body: RequestBody =
            reqJson.toRequestBody("application/json".toMediaTypeOrNull())
        compositeDisposable.add(repository.updateFcmToken(ps_auth_token, body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {

                },
                { t: Throwable? -> Log.i("Throwable", t?.message ?: "error") }
            )

        )
    }
}