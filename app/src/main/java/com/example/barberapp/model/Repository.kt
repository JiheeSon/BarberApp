package com.example.barberapp.model

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.example.barberapp.model.remote.ApiService
import com.example.barberapp.model.remote.request.LoginRequest
import com.example.barberapp.model.remote.request.RegistrationRequest
import com.example.barberapp.model.remote.response.DashboardResponse
import com.example.barberapp.model.remote.response.LoginResponse
import com.example.barberapp.model.remote.response.RegistrationResponse
import com.example.barberapp.model.remote.response.appointment.AppointmentResponse
import com.example.barberapp.model.remote.response.barber.BarbersResponse
import com.example.barberapp.model.remote.response.service.ServiceCategoryResponse
import com.example.barberapp.model.remote.response.service.ServiceResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository(private val apiService: ApiService) {
    val isProcessing = ObservableField<Boolean>()
    val error = MutableLiveData<String>()

    val registrationResponse = MutableLiveData<RegistrationResponse>()
    val loginResponse = MutableLiveData<LoginResponse>()
    val barbersResponse = MutableLiveData<BarbersResponse>()
    val serviceCategoryResponse = MutableLiveData<ServiceCategoryResponse>()
    val dashboardResponse = MutableLiveData<DashboardResponse>()

    val appointmentError = MutableLiveData<String>()
    val appointmentResponse = MutableLiveData<AppointmentResponse>()
    val appointmentProcessing = MutableLiveData<Boolean>()
    fun bookAppointment(params: HashMap<String, String>) {
        isProcessing.set(true)
        appointmentProcessing.postValue(true)
        val call = apiService.bookAppointment(params)
        call.enqueue(object : Callback<AppointmentResponse> {
            override fun onResponse(
                call: Call<AppointmentResponse>,
                response: Response<AppointmentResponse>
            ) {
                isProcessing.set(false)
                appointmentProcessing.postValue(false)
                if(!response.isSuccessful) {
                    appointmentError.postValue("Failed to book. Error code: ${response.code()}")
                } else {
                    val apiResponse = response.body()
                    if (apiResponse == null) {
                        appointmentError.postValue("Empty response. Please retry.")
                    } else {
                        if (apiResponse.status == 0) {
                            appointmentResponse.postValue(apiResponse)
                        } else {
                            appointmentError.postValue(apiResponse.message)
                        }
                    }
                }
            }
            override fun onFailure(call: Call<AppointmentResponse>, t: Throwable) {
                isProcessing.set(false)
                appointmentProcessing.postValue(false)
                t.printStackTrace()
                appointmentError.postValue("Error is : ${t.toString()}.\n\nPlease retry.")
            }

        })
    }

    suspend fun getBarberServices() = apiService.getBarberServices()
    suspend fun getAlert() = apiService.getAlert()

    fun getDashboard() {
        isProcessing.set(true)
        val call = apiService.getDashboard()
        call.enqueue(object : Callback<DashboardResponse> {
            override fun onResponse(
                call: Call<DashboardResponse>,
                response: Response<DashboardResponse>
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
                            dashboardResponse.postValue(apiResponse)
                        } else {
                            error.postValue(apiResponse.message)
                        }
                    }
                }
            }
            override fun onFailure(call: Call<DashboardResponse>, t: Throwable) {
                isProcessing.set(false)
                t.printStackTrace()
                error.postValue("Error is : ${t.toString()}.\n\nPlease retry.")
            }

        })
    }

    suspend fun getServiceByCategory(id: String): Response<ServiceResponse> {
        isProcessing.set(true)
        return apiService.getServiceByCategory(id)
    }

    fun getServiceCategory() {
        isProcessing.set(true)
        val call = apiService.getServiceCategory()
        call.enqueue(object : Callback<ServiceCategoryResponse> {
            override fun onResponse(
                call: Call<ServiceCategoryResponse>,
                response: Response<ServiceCategoryResponse>
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
                            serviceCategoryResponse.postValue(apiResponse)
                        } else {
                            error.postValue(apiResponse.message)
                        }
                    }
                }
            }
            override fun onFailure(call: Call<ServiceCategoryResponse>, t: Throwable) {
                isProcessing.set(false)
                t.printStackTrace()
                error.postValue("Error is : ${t.toString()}.\n\nPlease retry.")
            }

        })
    }

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