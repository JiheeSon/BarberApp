package com.example.barberapp.model.remote.response.history

data class AppointmentInfo(
    val aptDate: String,
    val aptNo: Int,
    val aptStatus: String,
    val timeFrom: String,
    val timeTo: String,
    val totalDuration: Double
)