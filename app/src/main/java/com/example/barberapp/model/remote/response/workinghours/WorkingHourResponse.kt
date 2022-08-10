package com.example.barberapp.model.remote.response.workinghours

data class WorkingHourResponse(
    val message: String,
    val status: Int,
    val timings: Map<String, Weekday>
)