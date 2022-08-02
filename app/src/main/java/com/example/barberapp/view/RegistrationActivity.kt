package com.example.barberapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.barberapp.R
import com.example.barberapp.databinding.ActivityRegistrationBinding
import com.example.barberapp.model.Repository
import com.example.barberapp.model.remote.ApiService
import com.example.barberapp.viewmodel.AuthVMFactory
import com.example.barberapp.viewmodel.AuthViewModel
import com.google.firebase.messaging.FirebaseMessaging
import org.json.JSONArray

class RegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var viewModel: AuthViewModel

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

        setUpSpinner()
        setUpViewModel()
        setUpInputObserver()
        setUpApiObserver()
    }

    private fun setUpSpinner() {
        val jsonString = assets.open("CountryCodes.json").reader().readText()
        val jsonArray = JSONArray(jsonString)
        val adapter = CodeSpinnerAdapter(this, jsonArray)
        binding.spinnerCode.adapter = adapter

        binding.spinnerCode.setSelection(230)
        var selected = ""
        binding.spinnerCode.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selected = jsonArray.getJSONObject(p2).getString("dial_code")
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    private fun setUpApiObserver() {
        viewModel.registrationResponse.observe(this) {
            openDialog(it!!.status, it.message)
        }

        viewModel.error.observe(this) {
            openDialog(-1, it!!)
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
        viewModel.mobileError.observe(this) {
            binding.editMobile.error = it
        }
        viewModel.passwordError.observe(this) {
            binding.editPassword.error = it
        }
        viewModel.confirmPasswordError.observe(this) {
            binding.editConfirmPassword.error = it
        }
    }

    private fun setUpViewModel() {
        val vmFactory = AuthVMFactory(Repository(ApiService.getInstance()))
        viewModel = ViewModelProvider(this, vmFactory)[AuthViewModel::class.java]
        binding.viewModel = viewModel
    }



}