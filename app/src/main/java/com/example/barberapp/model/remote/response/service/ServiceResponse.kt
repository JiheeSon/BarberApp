package com.example.barberapp.model.remote.response.service

data class ServiceResponse(
    val message: String,
    val services: List<Service>,
    val status: Int
)