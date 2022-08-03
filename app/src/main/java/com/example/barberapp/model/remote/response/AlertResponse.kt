package com.example.barberapp.model.remote.response

data class AlertResponse(
    val alert: List<Alert>,
    val message: String,
    val status: Int
)