package com.example.barberapp.view.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.barberapp.R
import com.example.barberapp.databinding.ActivityLoginBinding
import com.example.barberapp.model.Repository
import com.example.barberapp.model.remote.ApiService
import com.example.barberapp.model.remote.response.LoginResponse
import com.example.barberapp.view.dashboard.DashboardActivity
import com.example.barberapp.viewmodel.*
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.noAccount.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isSuccessful) {
                Log.i("tag", it.result)
            }
        }

        setupViewModel()
        setupObservers()
    }

    private fun setupViewModel() {
        val vmFactory = AuthVMFactory(Repository(ApiService.getInstance()))
        viewModel = ViewModelProvider(this, vmFactory)[AuthViewModel::class.java]
        binding.viewModel = viewModel
    }

    private fun setupObservers() {
        viewModel.loginResponse.observe(this) {
            FirebaseMessaging.getInstance().token.addOnCompleteListener {
                if (it.isSuccessful) {
                    viewModel.updateFcmToken(it.result)
                }
            }
            saveUser(it)
            startActivity(Intent(baseContext, DashboardActivity::class.java))
            finish()
        }

        viewModel.error.observe(this) {
            openDialog(it!!)
        }
    }

    private fun openDialog(message: String) {
        val builder = AlertDialog.Builder(this)
            .setTitle("Sorry")
            .setIcon(R.drawable.ic_baseline_check_24)
            .setMessage(message)
            .setNeutralButton("Try again", null)
        val alertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun saveUser(user: LoginResponse) {
        val pref = getSharedPreferences("user_info", MODE_PRIVATE)

        pref.edit().apply{
            putString("mobile_no", user.mobileNo)
            putString("user_Id", user.userId)
            putString("api_token", user.apiToken)
            putString("fcm_token", user.fcmToken)
            apply()
        }
    }
}