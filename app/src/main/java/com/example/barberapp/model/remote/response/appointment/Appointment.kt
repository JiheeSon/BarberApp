package com.example.barberapp.model.remote.response.appointment

data class Appointment(
    val aptDate: String,
    val aptNo: Int,
    val aptStatus: String,
    val barberId: Int,
    val barberName: String,
    val couponCode: String,
    val couponDiscount: Double,
    val finalCost: Double,
    val fullName: String,
    val mobileNo: String,
    val previousTimePhotos: String,
    val profilePic: String,
    val services: List<Service>,
    val timeFrom: String,
    val timeTo: String,
    val totalCost: Double,
    val totalDuration: Double,
    val userId: Int,
    val userProfilePic: String
)