package com.example.barberapp.model.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    val retrofit by lazy {
        val logInterceptor = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

        val client = OkHttpClient.Builder().apply {
            addInterceptor(logInterceptor)
        }.build()

        val r = Retrofit.Builder().apply {
            baseUrl("https://psmobitech.com/barberapp/v3/index.php/api/")
            addConverterFactory(GsonConverterFactory.create())
            client(client)
        }.build()

        r   // return Retrofit object
    }
}