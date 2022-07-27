package com.example.barberapp.model.remote.request

data class RegistrationRequest(
    val fcmToken: String,
    val mobileNo: String,
    val password: String
)