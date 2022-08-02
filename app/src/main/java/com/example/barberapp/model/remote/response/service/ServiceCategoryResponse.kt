package com.example.barberapp.model.remote.response.service

data class ServiceCategoryResponse(
    val message: String,
    val serviceCategories: List<ServiceCategory>,
    val status: Int
)