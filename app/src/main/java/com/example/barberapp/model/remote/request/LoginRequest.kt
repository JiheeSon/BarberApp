package com.example.barberapp.model.remote.request

data class LoginRequest(
    val password: String,
    val username: String
)