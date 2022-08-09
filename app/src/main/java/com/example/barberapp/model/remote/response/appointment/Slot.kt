package com.example.barberapp.model.remote.response.appointment

data class Slot(
    val date: String,
    val day: String,
    val slots: Map<String, Boolean>
)