package com.example.barberapp.model.remote.response.barber

data class Barber(
    val barberId: Int,
    val barberName: String,
    val breakTimeFrom: String,
    val breakTimeTo: String,
    val gender: String,
    val hasDefaultServices: Int,
    val holiday: String,
    val isAdmin: Int,
    val isBarber: Int,
    val mobileNo: String,
    val password: String,
    val payment: Double,
    val profilePic: String,
    val type: String,
    val userRating: Double
)