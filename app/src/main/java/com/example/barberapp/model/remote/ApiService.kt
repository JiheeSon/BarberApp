package com.example.barberapp.model.remote

import com.example.barberapp.model.remote.request.LoginRequest
import com.example.barberapp.model.remote.request.RegistrationRequest
import com.example.barberapp.model.remote.response.LoginResponse
import com.example.barberapp.model.remote.response.RegistrationResponse
import retrofit2.Call
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    companion object {
        fun getInstance(): ApiService = ApiClient.retrofit.create(ApiService::class.java)
    }

    @POST("user/signup")
    @Headers("Content-type: application/json")
    fun signUpUser(
        @Body registrationRequest: RegistrationRequest
    ): Call<RegistrationResponse>


    @POST("user/login")
    @Headers("Content-type: application/json")
    fun loginUser(
        @Body loginRequest: LoginRequest
    ): Call<LoginResponse>
}