package com.example.barberapp.model.remote.response.contacts

data class ContactResponse(
    val contacts: List<Contact>,
    val message: String,
    val status: Int
)