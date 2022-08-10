package com.example.barberapp.model

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.example.barberapp.model.remote.ApiService
import com.example.barberapp.model.remote.request.LoginRequest
import com.example.barberapp.model.remote.request.RegistrationRequest
import com.example.barberapp.model.remote.response.DashboardResponse
import com.example.barberapp.model.remote.response.LoginResponse
import com.example.barberapp.model.remote.response.RegistrationResponse
import com.example.barberapp.model.remote.response.appointment.AppointmentResponse
import com.example.barberapp.model.remote.response.appointment.CurrentAppointmentsResponse
import com.example.barberapp.model.remote.response.appointment.Slot
import com.example.barberapp.model.remote.response.barber.BarbersResponse
import com.example.barberapp.model.remote.response.service.ServiceCategoryResponse
import com.example.barberapp.model.remote.response.service.ServiceResponse
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
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

    fun getAppointmentDetail(token: String, appointmentId: String) = apiService.getAppointmentDetail(token, appointmentId)
    fun getAppointments(token: String, userId: String) = apiService.getAppointments(token, userId)
    fun cancelAppointment(token: String, appointmentId: String) = apiService.cancelAppointment(token, appointmentId)

    val appointmentError = MutableLiveData<String>()
    val appointmentResponse = MutableLiveData<AppointmentResponse>()
    val appointmentProcessing = MutableLiveData<Boolean>()
    fun bookAppointment(ps_auth_token: String, params: HashMap<String, Any>) {
        isProcessing.set(true)
        appointmentProcessing.postValue(true)

        val reqJson: String = Gson().toJson(params)
        val body: RequestBody = reqJson.toRequestBody("application/json".toMediaTypeOrNull())

        val call = apiService.bookAppointment(ps_auth_token, body)
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

    val currentAppointmentsLiveData = MutableLiveData<List<Slot>>()
    fun getCurrentAppointments(barberId: String) {
        val call = apiService.getCurrentAppointments(barberId)
        call.enqueue(object : Callback<CurrentAppointmentsResponse> {
            override fun onResponse(
                call: Call<CurrentAppointmentsResponse>,
                response: Response<CurrentAppointmentsResponse>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == 0) {
                        Log.e("loadCurrentAppointments", response.body()!!.slots.toString())
                        currentAppointmentsLiveData.postValue(response.body()!!.slots)

                    } else {
                        Log.e("response error", response.body()!!.message)
                    }
                }
            }

            override fun onFailure(call: Call<CurrentAppointmentsResponse>, t: Throwable) {
                Log.e("response.body()", t.toString())
                t.printStackTrace()
            }
        })
    }

    suspend fun getContacts() = apiService.getContacts()
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