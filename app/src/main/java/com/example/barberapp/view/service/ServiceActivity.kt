package com.example.barberapp.view.service

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.barberapp.databinding.ActivityServiceBinding
import com.example.barberapp.model.Repository
import com.example.barberapp.model.remote.ApiService
import com.example.barberapp.viewmodel.ServiceVMFactory
import com.example.barberapp.viewmodel.ServiceViewModel

class ServiceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityServiceBinding
    private lateinit var viewModel: ServiceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpViewModel()
        viewModel.getService()
        setUpObserver()
    }

    private fun setUpObserver() {
        viewModel.serviceCategoryResponse.observe(this) {
            Log.d("jihee", it.toString())
        }

        viewModel.services.observe(this) {
            Log.d("jihee", it.toString())
        }
    }

    private fun setUpViewModel() {
        val vmFactory = ServiceVMFactory(Repository(ApiService.getInstance()))
        viewModel = ViewModelProvider(this, vmFactory)[ServiceViewModel::class.java]
    }
}