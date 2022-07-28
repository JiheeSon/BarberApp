package com.example.barberapp.model.remote.response

data class LoginResponse(
    val apiToken: String,
    val createdOn: String,
    val dateOfBirth: String,
    val deletedOn: String,
    val emailId: String,
    val emailVerificationCode: String,
    val evcExpiresOn: String,
    val fcmToken: String,
    val fullName: String,
    val gender: String,
    val ipAddresse: String,
    val isActive: String,
    val isDeleted: String,
    val isEmailVerified: String,
    val isMobileVerified: String,
    val message: String,
    val mobileNo: String,
    val password: String,
    val profilePic: String,
    val status: Int,
    val tokenValidUpto: String,
    val updatedOn: String,
    val userId: String
)