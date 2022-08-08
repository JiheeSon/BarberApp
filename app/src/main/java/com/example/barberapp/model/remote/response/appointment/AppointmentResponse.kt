package com.example.barberapp.model.remote.response.appointment

data class AppointmentResponse(
    val appointment: Appointment,
    val message: String,
    val status: Int
)