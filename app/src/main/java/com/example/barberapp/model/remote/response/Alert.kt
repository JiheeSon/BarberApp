package com.example.barberapp.model.remote.response

data class Alert(
    val createdOn: String,
    val id: Int,
    val message: String,
    val type: String
)