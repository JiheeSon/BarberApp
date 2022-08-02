package com.example.barberapp.model.remote.response.barber

data class BarbersResponse(
    val barbers: List<Barber>,
    val message: String,
    val status: Int
)