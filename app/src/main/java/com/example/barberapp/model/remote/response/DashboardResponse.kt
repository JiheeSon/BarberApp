package com.example.barberapp.model.remote.response

data class DashboardResponse(
    val alertMessage: String,
    val banners: List<Banner>,
    val isShopOpened: String,
    val message: String,
    val status: Int
)