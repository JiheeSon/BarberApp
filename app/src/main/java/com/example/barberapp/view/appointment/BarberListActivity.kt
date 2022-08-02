package com.example.barberapp.view.appointment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.barberapp.R
import com.example.barberapp.databinding.ActivityBarberListBinding
import com.example.barberapp.model.Repository
import com.example.barberapp.model.remote.ApiService
import com.example.barberapp.viewmodel.AppointmentVMFactory
import com.example.barberapp.viewmodel.AppointmentViewModel
import com.example.barberapp.viewmodel.AuthVMFactory
import com.example.barberapp.viewmodel.AuthViewModel

class BarberListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBarberListBinding
    private lateinit var viewModel: AppointmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBarberListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpViewModel()
        viewModel.getBarberList()
        setUpView()
    }

    private fun setUpView() {
        viewModel.barbersResponse.observe(this@BarberListActivity) {
            val adapter = BarberListAdapter(it.barbers, viewModel)
            binding.recyclerviewBarbers.layoutManager = GridLayoutManager(this@BarberListActivity, 2)
            binding.recyclerviewBarbers.adapter = adapter
        }
    }

    private fun setUpViewModel() {
        val vmFactory = AppointmentVMFactory(Repository(ApiService.getInstance()))
        viewModel = ViewModelProvider(this@BarberListActivity, vmFactory)[AppointmentViewModel::class.java]
    }
}