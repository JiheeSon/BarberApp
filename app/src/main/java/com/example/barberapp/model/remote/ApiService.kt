package com.example.barberapp.model.remote

import com.example.barberapp.model.remote.request.LoginRequest
import com.example.barberapp.model.remote.request.RegistrationRequest
import com.example.barberapp.model.remote.response.*
import com.example.barberapp.model.remote.response.appointment.AppointmentResponse
import com.example.barberapp.model.remote.response.appointment.CurrentAppointmentsResponse
import com.example.barberapp.model.remote.response.barber.BarberServiceResponse
import com.example.barberapp.model.remote.response.barber.BarbersResponse
import com.example.barberapp.model.remote.response.contacts.ContactResponse
import com.example.barberapp.model.remote.response.history.GetAppointmentsResponse
import com.example.barberapp.model.remote.response.service.ServiceCategoryResponse
import com.example.barberapp.model.remote.response.service.ServiceResponse
import com.example.barberapp.model.remote.response.workinghours.WorkingHourResponse
import io.reactivex.rxjava3.core.Single
import okhttp3.RequestBody
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
    ): Response<ServiceResponse>


    @GET("/user/dashboard")
    fun getDashboard(): Call<DashboardResponse>


    @GET("/alert/getList")
    suspend fun getAlert(): Response<AlertResponse>


    @POST("/barber/getBarberServices1")
    suspend fun getBarberServices(): Response<BarberServiceResponse>


    @Headers("Content-type: application/json")
    @POST("/appointment/book")
    fun bookAppointment(
        @Header("ps_auth_token") ps_auth_token: String,
        //@QueryMap params: HashMap<String, String>
        @Body bookReq: RequestBody
    ): Call<AppointmentResponse>


    @GET("shopContacts/getList")
    suspend fun getContacts(): Response<ContactResponse>


    @GET("appointment/currentAppointments/{barber_id}")
    fun getCurrentAppointments(
        @Path("barber_id") barberId: String
    ): Call<CurrentAppointmentsResponse>


    @Headers("Content-type: application/json")
    @GET("appointment/myAppointments/{user_id}")
    fun getAppointments(
        @Header("ps_auth_token") ps_auth_token: String,
        @Path("user_id") userId: String
    ): Call<GetAppointmentsResponse>


    @Headers("Content-type: application/json")
    @GET("appointment/getAppointmentDetail/{appointment_id}")
    fun getAppointmentDetail(
        @Header("ps_auth_token") ps_auth_token: String,
        @Path("appointment_id") appointmentId: String
    ): Call<AppointmentResponse>


    @GET("appointment/cancelAppointment/{appointment_id}")
    fun cancelAppointment(
        @Header("ps_auth_token") ps_auth_token: String,
        @Path("appointment_id") appointmentId: String
    ): Call<AppointmentResponse>


    @Headers("Content-type: application/json")
    @GET("workingHours/getList")
    fun getWorkingHours(): Single<WorkingHourResponse>


    @Headers("Content-type: application/json")
    @POST("appUser/updateFcmToken")
    fun updateFcmToken(
        @Header("ps_auth_token") ps_auth_token: String,
        @Body updateReq: RequestBody
    ): Single<BaseResponse>

    @Headers("Content-type: application/json")
    @POST("/appointment/reschedule")
    fun rescheduleAppointment(
        @Header("ps_auth_token") ps_auth_token: String,
        @Body request: RequestBody
    ): Call<AppointmentResponse>
}