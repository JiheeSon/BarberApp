package com.example.barberapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.barberapp.R
import com.example.barberapp.databinding.ActivityLoginBinding
import com.example.barberapp.model.Repository
import com.example.barberapp.viewmodel.LoginVMFactory
import com.example.barberapp.viewmodel.LoginViewModel
import com.example.barberapp.viewmodel.RegistrationVMFactory
import com.example.barberapp.viewmodel.RegistrationViewModel
import com.google.firebase.messaging.FirebaseMessaging

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

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
        val vmFactory = LoginVMFactory(Repository())
        viewModel = ViewModelProvider(this, vmFactory)[LoginViewModel::class.java]
        binding.viewModel = viewModel
    }

    private fun setupObservers() {
        viewModel.loginResponse.observe(this) {
            //saveUser(it.user)
            startActivity(Intent(baseContext, DashboardActivity::class.java))
            finish()
            //Toast.makeText(this, it.message, Toast.LENGTH_SHORT)
        }

        viewModel.error.observe(this) {
            //Toast.makeText(baseContext, it, Toast.LENGTH_SHORT).show()
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

//    private fun saveUser(user: User) {
//        val pref = getSharedPreferences("users", MODE_PRIVATE)
//
//        pref.edit().apply{
//            putString("name", user.name)
//            putString("mobile_no", user.mobile_no)
//            putString("user_id", user.user_id)
//            apply()
//        }
//    }
}