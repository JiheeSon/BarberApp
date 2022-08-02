package com.example.barberapp.view.appointment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.barberapp.R
import com.example.barberapp.databinding.ActivityAppointmentBinding
import com.example.barberapp.model.Repository
import com.example.barberapp.model.remote.ApiService
import com.example.barberapp.viewmodel.AppointmentVMFactory
import com.example.barberapp.viewmodel.AppointmentViewModel

class AppointmentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAppointmentBinding
    private lateinit var viewModel: AppointmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment, BarberListFragment())
            .commit()

        setUpViewModel()
        viewModel.getBarberList()
    }

    private fun setUpViewModel() {
        val vmFactory = AppointmentVMFactory(Repository(ApiService.getInstance()))
        viewModel = ViewModelProvider(this@AppointmentActivity, vmFactory)[AppointmentViewModel::class.java]
    }
}