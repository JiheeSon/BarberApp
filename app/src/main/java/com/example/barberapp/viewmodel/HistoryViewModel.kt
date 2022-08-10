package com.example.barberapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.barberapp.model.Repository
import com.example.barberapp.model.remote.response.appointment.Appointment
import com.example.barberapp.model.remote.response.appointment.AppointmentResponse
import com.example.barberapp.model.remote.response.history.AppointmentInfo
import com.example.barberapp.model.remote.response.history.GetAppointmentsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryViewModel(private val repository: Repository): ViewModel() {
    val apiToken = MutableLiveData<String>()
    val userId = MutableLiveData<String>()
    val selectedAppointmentNum = MutableLiveData<String>()

    val appointmentLiveData = MutableLiveData<Appointment>()
    fun getAppointmentDetail() {
        val call: Call<AppointmentResponse> = repository.getAppointmentDetail(apiToken.value!!, selectedAppointmentNum.value!!)
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
                            "loadCurrentAppointments",
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
}