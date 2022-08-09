package com.example.barberapp.view.appointment.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.barberapp.R
import com.example.barberapp.databinding.FragmentBarberListBinding
import com.example.barberapp.model.remote.response.barber.Barber
import com.example.barberapp.view.appointment.AppointmentActivity
import com.example.barberapp.view.appointment.adapter.BarberListAdapter
import com.example.barberapp.viewmodel.AppointmentViewModel

class BarberListFragment : Fragment() {
    private lateinit var binding: FragmentBarberListBinding
    private lateinit var viewModel: AppointmentViewModel
    private lateinit var adapter: BarberListAdapter
    private lateinit var barberList: List<Barber>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBarberListBinding.inflate(layoutInflater, container, false)
        val toolbar = (activity as AppointmentActivity).supportActionBar
        toolbar?.apply {
            setTitle("Select Barber")
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(AppointmentViewModel::class.java)
        setUpView()
        setUpEvent()
    }

    private fun setUpEvent() {
        viewModel.selectedBarber.observe(requireActivity()) {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment, BarberServiceFragment())
                //.addToBackStack(null)
                .commit()
        }
    }

    private fun setUpView() {
        viewModel.barbersResponse.observe(requireActivity()) {
            adapter = BarberListAdapter(it.barbers, viewModel)
            binding.recyclerviewBarbers.layoutManager = GridLayoutManager(context, 2)
            binding.recyclerviewBarbers.adapter = adapter
        }
    }
}