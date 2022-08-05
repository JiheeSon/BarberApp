package com.example.barberapp.model.remote

import com.example.barberapp.model.remote.request.LoginRequest
import com.example.barberapp.model.remote.request.RegistrationRequest
import com.example.barberapp.model.remote.response.AlertResponse
import com.example.barberapp.model.remote.response.DashboardResponse
import com.example.barberapp.model.remote.response.LoginResponse
import com.example.barberapp.model.remote.response.RegistrationResponse
import com.example.barberapp.model.remote.response.barber.BarberServiceResponse
import com.example.barberapp.model.remote.response.barber.BarbersResponse
import com.example.barberapp.model.remote.response.service.ServiceCategoryResponse
import com.example.barberapp.model.remote.response.service.ServiceResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

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

    @GET("/barber/getBarbers")
    fun getBarbers(): Call<BarbersResponse>

    @GET("/service/getServiceCategory")
    fun getServiceCategory(): Call<ServiceCategoryResponse>

    @GET("/service/category/{category_id}")
    suspend fun getServiceByCategory(
        @Path("category_id") id: String
    ): ServiceResponse

    @GET("/user/dashboard")
    fun getDashboard(): Call<DashboardResponse>

    @GET("/alert/getList")
    suspend fun getAlert(): Response<AlertResponse>

    @POST("/barber/getBarberServices1")
    suspend fun getBarberServices(): Response<BarberServiceResponse>
}