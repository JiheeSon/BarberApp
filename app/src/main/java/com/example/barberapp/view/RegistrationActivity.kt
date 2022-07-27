package com.example.barberapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.barberapp.R
import com.example.barberapp.databinding.ActivityRegistrationBinding
import com.example.barberapp.model.Repository
import com.example.barberapp.viewmodel.RegistrationViewModel
import com.example.barberapp.viewmodel.RegistrationVMFactory
import com.google.firebase.messaging.FirebaseMessaging

class RegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var viewModel: RegistrationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.haveAccount.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isSuccessful) {
                viewModel.fcmToken.value = it.result
            }
        }

        setUpViewModel()
        setUpInputObserver()
        setUpApiObserver()
    }

    private fun setUpApiObserver() {
        viewModel.registrationResponse.observe(this) {
            openDialog(it!!.status, it.message)
        }

        viewModel.error.observe(this) {
            openDialog(1, it!!)
        }
    }

    private fun openDialog(status: Int, message: String) {
        val title = if (status == 0) "Welcome!" else "Sorry"
        val builder = AlertDialog.Builder(this)
            .setTitle(title)
            .setIcon(R.drawable.ic_baseline_check_24)
            .setMessage(message)
            .setPositiveButton("Go to Login") {_, _ -> finish() }
            .setNeutralButton("Try again", null)
        val alertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun setUpInputObserver() {
        viewModel.isMobileNotEmpty.observe(this) {
            binding.editMobile.error = if (it!!) null else "Required"
        }

        viewModel.isMobileValid.observe(this) {
            binding.editMobile.error = if (it!!) null else "Invalid Number"
        }

        viewModel.isPasswordNotEmpty.observe(this) {
            binding.editPassword.error = if (it!!) null else "Required"
        }

        viewModel.isConfirmNotEmpty.observe(this) {
            binding.editConfirmPassword.error = if (it!!) null else "Required"
        }

        viewModel.matchPassword.observe(this) {
            binding.editConfirmPassword.error = if (it!!) null else "Please check again"
        }
    }

    private fun setUpViewModel() {
        val vmFactory = RegistrationVMFactory(Repository())
        viewModel = ViewModelProvider(this, vmFactory)[RegistrationViewModel::class.java]
        binding.viewModel = viewModel
    }



}