package com.example.barberapp.model

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.example.barberapp.model.remote.ApiClient
import com.example.barberapp.model.remote.ApiService
import com.example.barberapp.model.remote.request.RegistrationRequest
import com.example.barberapp.model.remote.response.RegistrationResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository() {
    private val retrofit = ApiClient.retrofit
    private val apiService = retrofit.create(ApiService::class.java)

    val isProcessing = ObservableField<Boolean>()
    val error = MutableLiveData<String>()
    val registrationResponse = MutableLiveData<RegistrationResponse>()

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