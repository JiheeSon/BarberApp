package com.example.barberapp.model.remote.response.history

data class GetAppointmentsResponse(
    val appointments: List<AppointmentInfo>,
    val message: String,
    val status: Int
)