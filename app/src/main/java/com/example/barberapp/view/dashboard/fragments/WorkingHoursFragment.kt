package com.example.barberapp.view.dashboard.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.barberapp.R
import com.example.barberapp.databinding.FragmentWorkingHoursBinding
import com.example.barberapp.view.dashboard.adapter.WorkingHourAdapter
import com.example.barberapp.viewmodel.DashboardViewModel

class WorkingHoursFragment : Fragment() {
    private lateinit var binding: FragmentWorkingHoursBinding
    private lateinit var viewModel: DashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWorkingHoursBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[DashboardViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getWorkingHour()
        binding.textStatus.text = viewModel.dashboardResponse.value!!.isShopOpened
        if (viewModel.dashboardResponse.value!!.isShopOpened.contains("Close")) {
            binding.textStatus.setTextColor(resources.getColor(R.color.main_red))
        } else {
            binding.textStatus.setTextColor(resources.getColor(R.color.teal_700))
        }

        viewModel.workingHourLiveData.observe(requireActivity()) {
            binding.circularProgressBar.visibility = View.GONE
            val adapter = WorkingHourAdapter(this, viewModel, it)
            binding.recyclerviewHours.adapter = adapter
            binding.recyclerviewHours.layoutManager = LinearLayoutManager(this.context)
        }
    }
}