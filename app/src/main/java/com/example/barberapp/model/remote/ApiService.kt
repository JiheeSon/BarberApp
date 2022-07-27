package com.example.barberapp.model.remote

import com.example.barberapp.model.remote.request.RegistrationRequest
import com.example.barberapp.model.remote.response.RegistrationResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @Headers("Content-type: application/json")
    @POST("AppUser/signup")
    fun signUpUser(@Body registrationRequest: RegistrationRequest): Call<RegistrationResponse>

}