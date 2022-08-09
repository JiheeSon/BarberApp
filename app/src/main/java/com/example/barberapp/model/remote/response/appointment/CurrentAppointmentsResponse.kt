package com.example.barberapp.model.remote.response.appointment

data class CurrentAppointmentsResponse(
    val message: String,
    val slots: List<Slot>,
    val status: Int
)