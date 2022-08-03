package com.example.barberapp.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.barberapp.R
import com.example.barberapp.databinding.FragmentHomeBinding
import com.example.barberapp.model.Repository
import com.example.barberapp.model.remote.ApiService
import com.example.barberapp.view.appointment.AppointmentActivity
import com.example.barberapp.view.service.ServiceActivity
import com.example.barberapp.viewmodel.AppointmentViewModel
import com.example.barberapp.viewmodel.DashboardVMFactory
import com.example.barberapp.viewmodel.DashboardViewModel

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: DashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        //binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewModel()
        setUpEvents()
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProvider(requireActivity()).get(DashboardViewModel::class.java)
        viewModel.dashboardResponse.observe(requireActivity()){
            binding.isOpen = it.isShopOpened
        }
    }

    private fun setUpEvents() {
        binding.apply {
            btnReserve.setOnClickListener { startActivity(Intent(context, AppointmentActivity::class.java)) }
            btnService.setOnClickListener { startActivity(Intent(context, ServiceActivity::class.java)) }
            btnHours.setOnClickListener {  }
            btnMore.setOnClickListener {  }
        }
    }

}