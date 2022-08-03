package com.example.barberapp.view.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.barberapp.R
import com.example.barberapp.databinding.FragmentHomeBinding
import com.example.barberapp.view.appointment.AppointmentActivity
import com.example.barberapp.view.service.ServiceActivity
import com.example.barberapp.viewmodel.DashboardViewModel

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: DashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        initToolBar()
        //binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewModel()
        setUpEvents()
        setUpViews()
    }

    private fun setUpViews() {
        viewModel.barbersResponse.observe(requireActivity()) {
            val adapter = BarberProfileAdapter(it.barbers)
            binding.recyclerviewBarbers.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerviewBarbers.adapter = adapter
        }

        viewModel.categoryResponse.observe(requireActivity()) {
            val adapter = CategoryAdapter(it.serviceCategories)
            binding.viewpagerService.adapter = adapter
        }
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
            btnMore.setOnClickListener { (activity as DashboardActivity).openDrawer() }
        }
    }

    private fun initToolBar() {
        val toolbar = (activity as DashboardActivity).supportActionBar
        toolbar?.apply {
            setTitle("My Salon")
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)
        }
    }

}