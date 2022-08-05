package com.example.barberapp.model.remote.response.barber

data class BarberServiceResponse(
    val message: String,
    val services: List<Service>,
    val status: Int
)