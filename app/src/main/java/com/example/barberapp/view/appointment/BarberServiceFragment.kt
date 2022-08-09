package com.example.barberapp.view.appointment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.barberapp.R
import com.example.barberapp.databinding.FragmentBarberServiceBinding
import com.example.barberapp.view.appointment.adapter.ServiceSectionAdapter
import com.example.barberapp.viewmodel.AppointmentViewModel
import com.google.android.material.snackbar.Snackbar

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
        setUpEvent()
    }

    private fun setUpEvent() {
        binding.btnChangeBarber.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.btnContinue.setOnClickListener {
            if (viewModel.selectedServices.value == null || viewModel.selectedServices.value!!.isEmpty()) {
                Snackbar.make(binding.root, "Please select at least on service to proceed", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Confirm") {
                    }
                    .show()
            } else {
                viewModel.computeTotalDurationAndCost()
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment, TimeSelectFragment2())
                    //.addToBackStack(null)
                    .commit()
            }
//            viewModel.selectedServices.observe(requireActivity()) {
//                Log.i("jihee", it.toString())
//            }
//            if (viewModel.selectedServices.value?.isNotEmpty()!!) {
//                requireActivity().supportFragmentManager.beginTransaction()
//                    .replace(R.id.fragment, TimeSelectFragment())
//                    //.addToBackStack(null)
//                    .commit()
//            } else {
//                Snackbar.make(binding.root, "Please select at least on service to proceed", Snackbar.LENGTH_INDEFINITE)
//                    .setAction("Confirm") {
//                    }
//                    .show()
//            }
        }
    }

    private fun setUpView() {
        viewModel.barberServiceCategories.observe(requireActivity()) {
            val adapter = ServiceSectionAdapter(it, viewModel.barberServiceItems.value!!, viewModel)
            binding.recyclerview.layoutManager = LinearLayoutManager(context)
            binding.recyclerview.adapter = adapter
        }
    }

}