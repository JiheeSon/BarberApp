package com.example.barberapp.model.util

fun isNotEmpty(string: String?): Boolean {
    return !(string == null || string.isEmpty())
}

fun isMobileValid(number: String): Boolean {
    return !(!(number.all { it in '0'..'9' }) || number.length != 10)
}

fun isPasswordValid(password: String): Boolean {
    return password.length >= 8
}

fun confirmPassword(str1: String, str2: String): Boolean {
    return str1 == str2
}