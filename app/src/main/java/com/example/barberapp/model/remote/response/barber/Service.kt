package com.example.barberapp.model.remote.response.barber

data class Service(
    val cost: Double,
    val duration: Double,
    val serviceId: Int,
    val serviceName: String,
    val servicePic: String,
    val serviceType: String
)