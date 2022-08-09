package com.example.barberapp.view.dashboard.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.barberapp.R
import com.example.barberapp.databinding.FragmentAlertBinding
import com.example.barberapp.view.dashboard.adapter.AlertAdapter
import com.example.barberapp.view.dashboard.DashboardActivity
import com.example.barberapp.viewmodel.DashboardViewModel

class AlertFragment : Fragment() {
    private lateinit var binding: FragmentAlertBinding
    private lateinit var viewModel: DashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /* Inflate the layout for this fragment */
        binding = FragmentAlertBinding.inflate(layoutInflater, container, false)
        initToolBar()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewModel()
    }

    private fun initToolBar() {
        val toolbar = (activity as DashboardActivity).supportActionBar
        toolbar?.apply {
            setTitle("Notifications")
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        (activity as DashboardActivity).onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProvider(requireActivity()).get(DashboardViewModel::class.java)
        viewModel.alertLiveData.observe(requireActivity()) {
            val adapter = AlertAdapter(it.alert)
            binding.recyclerviewAlerts.layoutManager = LinearLayoutManager(context)
            binding.recyclerviewAlerts.adapter = adapter
        }
    }
}