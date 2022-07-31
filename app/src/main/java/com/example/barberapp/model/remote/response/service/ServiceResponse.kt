package com.example.barberapp.model.remote.response.service

data class ServiceResponse(
    val message: String,
    val services: Services,
    val status: Int
)