package com.example.barberapp.model.remote.response.service

data class Service(
    val cost: Double,
    val description: String,
    val duration: Double,
    val serviceId: Int,
    val serviceName: String,
    val servicePic: String,
    val serviceType: String
)