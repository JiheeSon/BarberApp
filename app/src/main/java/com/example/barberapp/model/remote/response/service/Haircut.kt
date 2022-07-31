package com.example.barberapp.model.remote.response.service

data class Haircut(
    val cost: Double,
    val duration: Double,
    val serviceId: Int,
    val serviceName: String,
    val servicePic: String
)