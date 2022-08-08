package com.example.barberapp.model.remote.response.appointment

data class Service(
    val cost: Double,
    val duration: Double,
    val serviceId: Int,
    val serviceName: String,
    val servicePic: String,
    val serviceType: String
)