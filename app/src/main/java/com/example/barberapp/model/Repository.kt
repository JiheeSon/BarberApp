package com.example.barberapp.model

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.example.barberapp.model.remote.ApiClient
import com.example.barberapp.model.remote.ApiService
import com.example.barberapp.model.remote.request.LoginRequest
import com.example.barberapp.model.remote.request.RegistrationRequest
import com.example.barberapp.model.remote.response.LoginResponse
import com.example.barberapp.model.remote.response.RegistrationResponse
import com.example.barberapp.model.remote.response.barber.BarbersResponse
import com.example.barberapp.model.remote.response.service.ServiceResponse
import com.example.barberapp.viewmodel.ServiceViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository(private val apiService: ApiService) {
    val isProcessing = ObservableField<Boolean>()
    val error = MutableLiveData<String>()

    val registrationResponse = MutableLiveData<RegistrationResponse>()
    val loginResponse = MutableLiveData<LoginResponse>()
    val serviceResponse = MutableLiveData<ServiceResponse>()
    val barbersResponse = MutableLiveData<BarbersResponse>()

    fun getBarbers() {
        isProcessing.set(true)
        val call = apiService.getBarbers()
        call.enqueue(object : Callback<BarbersResponse> {
            override fun onResponse(
                call: Call<BarbersResponse>,
                response: Response<BarbersResponse>
            ) {
                isProcessing.set(false)
                if(!response.isSuccessful) {
                    error.postValue("Failed to login. Error code: ${response.code()}")
                } else {
                    val apiResponse = response.body()
                    if (apiResponse == null) {
                        error.postValue("Empty response. Please retry.")
                    } else {
                        if (apiResponse.status == 0) {
                            barbersResponse.postValue(apiResponse)
                        } else {
                            error.postValue(apiResponse.message)
                        }
                    }
                }
            }
            override fun onFailure(call: Call<BarbersResponse>, t: Throwable) {
                isProcessing.set(false)
                t.printStackTrace()
                error.postValue("Error is : ${t.toString()}.\n\nPlease retry.")
            }

        })
    }

    fun getService() {
        isProcessing.set(true)
        val call = apiService.getServices()
        call.enqueue(object : Callback<ServiceResponse> {
            override fun onResponse(
                call: Call<ServiceResponse>,
                response: Response<ServiceResponse>
            ) {
                isProcessing.set(false)
                if(!response.isSuccessful) {
                    error.postValue("Failed to login. Error code: ${response.code()}")
                } else {
                    val apiResponse = response.body()
                    if (apiResponse == null) {
                        error.postValue("Empty response. Please retry.")
                    } else {
                        if (apiResponse.status == 0) {
                            serviceResponse.postValue(apiResponse)
                        } else {
                            error.postValue(apiResponse.message)
                        }
                    }
                }
            }
            override fun onFailure(call: Call<ServiceResponse>, t: Throwable) {
                isProcessing.set(false)
                t.printStackTrace()
                error.postValue("Error is : ${t.toString()}.\n\nPlease retry.")
            }
        })
    }

    fun login(loginRequest: LoginRequest) {
        isProcessing.set(true)
        val call = apiService.loginUser(loginRequest)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                isProcessing.set(false)
                if(!response.isSuccessful) {
                    error.postValue("Failed to login. Error code: ${response.code()}")
                } else {
                    val apiResponse = response.body()
                    if (apiResponse == null) {
                        error.postValue("Empty response. Please retry.")
                    } else {
                        if (apiResponse.status == 0) {
                            loginResponse.postValue(apiResponse)
                        } else {
                            error.postValue(apiResponse.message)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                isProcessing.set(false)
                t.printStackTrace()
                error.postValue("Error is : ${t.toString()}.\n\nPlease retry.")
            }

        })
    }

    fun register(registrationRequest: RegistrationRequest) {
        isProcessing.set(true)
        val call = apiService.signUpUser(registrationRequest)

        call.enqueue(object : Callback<RegistrationResponse> {
            override fun onResponse(
                call: Call<RegistrationResponse>,
                response: Response<RegistrationResponse>
            ) {
                isProcessing.set(false)
                if(!response.isSuccessful) {
                    error.postValue("Failed to login. Error code: ${response.code()}")
                    return
                }

                val apiResponse = response.body()
                if(apiResponse == null) {
                    error.postValue("Empty response. Please retry.")
                    return
                }
                if(apiResponse.status == 0) {
                    registrationResponse.postValue(apiResponse)
                } else {
                    error.postValue(apiResponse.message)
                }
            }

            override fun onFailure(call: Call<RegistrationResponse>, t: Throwable) {
                isProcessing.set(false)
                t.printStackTrace()
                error.postValue("Error is : ${t.toString()}.\n\nPlease retry.")
            }

        })
    }
}