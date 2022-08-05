package com.example.barberapp.view.appointment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.barberapp.R
import com.example.barberapp.databinding.FragmentBarberServiceBinding
import com.example.barberapp.view.dashboard.DashboardActivity
import com.example.barberapp.viewmodel.AppointmentViewModel

class BarberServiceFragment : Fragment() {
    private lateinit var binding: FragmentBarberServiceBinding
    private lateinit var viewModel: AppointmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        initToolBar()
        viewModel = ViewModelProvider(requireActivity()).get(AppointmentViewModel::class.java)
        binding = FragmentBarberServiceBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    private fun initToolBar() {
        val toolbar = (activity as AppointmentActivity).supportActionBar
        toolbar?.apply {
            setTitle("Select Service")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
    }

    private fun setUpView() {
        Log.i("jihee", "hello")
        viewModel.barberServiceCategories.observe(requireActivity()) {
            Log.i("jihee", viewModel.barberServiceItems.value.toString())
            Log.i("jihee", viewModel.barberServiceItems.value!!.size.toString())
            val adapter = ServiceSectionAdapter(it, viewModel.barberServiceItems.value!!)
            binding.recyclerview.layoutManager = LinearLayoutManager(context)
            binding.recyclerview.adapter = adapter
        }
    }

}