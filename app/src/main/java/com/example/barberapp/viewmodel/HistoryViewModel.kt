package com.example.barberapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.barberapp.model.Repository
import com.example.barberapp.model.remote.response.appointment.Appointment
import com.example.barberapp.model.remote.response.appointment.AppointmentResponse
import com.example.barberapp.model.remote.response.history.AppointmentInfo
import com.example.barberapp.model.remote.response.history.GetAppointmentsResponse
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Error
import kotlin.math.roundToInt

class HistoryViewModel(private val repository: Repository): ViewModel() {
    val apiToken = MutableLiveData<String>()
    val userId = MutableLiveData<String>()
    val selectedAppointmentNum = MutableLiveData<String>()

    val appointmentLiveData = MutableLiveData<Appointment>()
    fun getAppointmentDetail(appointmentId: String = "-1") {
        var id = appointmentId
        if (appointmentId == "-1") {
            id = selectedAppointmentNum.value!!
        }
        val call: Call<AppointmentResponse> = repository.getAppointmentDetail(apiToken.value!!, id)
        call.enqueue(object : Callback<AppointmentResponse> {
            override fun onResponse(
                call: Call<AppointmentResponse>,
                response: Response<AppointmentResponse>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == 0) {
                        Log.e(
                            "getAppointmentDetail",
                            response.body()!!.appointment.toString()
                        )
                        appointmentLiveData.postValue(response.body()!!.appointment)
                        appointmentsSlotLiveData.postValue((response.body()!!.appointment.totalDuration / 15 + 0.5).roundToInt())

                    } else {
                        Log.e("response error", response.body()!!.message)
                    }
                }
            }

            override fun onFailure(call: Call<AppointmentResponse>, t: Throwable) {
                Log.e("response.body()", t.toString())
                t.printStackTrace()
            }
        })
    }

    val appointmentsLiveData = MutableLiveData<List<AppointmentInfo>>()
    fun getAppointments(token: String, userId: String) {
         val call: Call<GetAppointmentsResponse> = repository.getAppointments(token, userId)
        call.enqueue(object : Callback<GetAppointmentsResponse> {
            override fun onResponse(
                call: Call<GetAppointmentsResponse>,
                response: Response<GetAppointmentsResponse>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == 0) {
                        Log.e(
                            "getAppointments",
                            response.body()!!.appointments.toString()
                        )
                        appointmentsLiveData.postValue(response.body()!!.appointments)

                    } else {
                        Log.e("response error", response.body()!!.message)
                    }
                }
            }

            override fun onFailure(call: Call<GetAppointmentsResponse>, t: Throwable) {
                Log.e("response.body()", t.toString())
                t.printStackTrace()
            }
        })
    }

    fun cancelAppointment(appointmentId: Int) {
        val call: Call<AppointmentResponse> =
            repository.cancelAppointment(apiToken.value!!, appointmentId.toString())
        call.enqueue(object : Callback<AppointmentResponse> {
            override fun onResponse(
                call: Call<AppointmentResponse>,
                response: Response<AppointmentResponse>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == 0) {
                        Log.e(
                            "cancelAppointment",
                            response.body()!!.appointment.toString()
                        )
                        appointmentLiveData.postValue(response.body()!!.appointment)

                    } else {
                        Log.e("response error", response.body()!!.message)
                    }
                }
            }

            override fun onFailure(call: Call<AppointmentResponse>, t: Throwable) {
                Log.e("response.body()", t.toString())
                t.printStackTrace()
            }
        })
    }

    val appointmentsDateLiveData = MutableLiveData<String>()
    val appointmentsSlotLiveData = MutableLiveData<Int>()
    val appointmentsStartFromLiveData = MutableLiveData<Int>()
    val currentAppointmentsLiveData = repository.currentAppointmentsLiveData
    fun getCurrentAppointments() {
        repository.getCurrentAppointments(appointmentLiveData.value?.barberId.toString())
    }

    val rescheduleError = MutableLiveData<String>()
    fun rescheduleAppointment(map: HashMap<String, Any>) {
        val reqJson: String = Gson().toJson(map)
        val body: RequestBody =
            reqJson.toRequestBody("application/json".toMediaTypeOrNull())
        val ps_auth_token = apiToken.value!!
        val call: Call<AppointmentResponse> =
            repository.rescheduleAppointment(ps_auth_token, body)
        call.enqueue(object : Callback<AppointmentResponse> {
            override fun onResponse(
                call: Call<AppointmentResponse>,
                response: Response<AppointmentResponse>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == 0) {
                        Log.e(
                            "rescheduleAppointment",
                            response.body()!!.appointment.toString()
                        )
                        appointmentLiveData.postValue(response.body()!!.appointment)
                    } else {
                        Log.e("response error", response.body()!!.message)
                        rescheduleError.postValue(response.body()!!.message)
                    }
                }
            }

            override fun onFailure(call: Call<AppointmentResponse>, t: Throwable) {
                Log.e("response.body()", t.toString())
                t.printStackTrace()
                rescheduleError.postValue(t.toString())
            }
        })
    }
}