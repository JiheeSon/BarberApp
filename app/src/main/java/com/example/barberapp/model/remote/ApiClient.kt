package com.example.barberapp.model.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    val retrofit: Retrofit by lazy {
        val logInterceptor = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

        val client = OkHttpClient.Builder().apply {
            addInterceptor(logInterceptor)
        }.build()

        val retrofit = Retrofit.Builder().apply {
            baseUrl("https://passageoftime.me:2333/")
            addConverterFactory(GsonConverterFactory.create())
            client(client)
        }.build()

        retrofit   // return Retrofit object
    }
}