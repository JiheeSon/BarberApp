package com.example.barberapp.view.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.barberapp.R
import com.example.barberapp.databinding.FragmentAppointmentListBinding
import com.example.barberapp.viewmodel.HistoryViewModel

class AppointmentListFragment : Fragment() {
    private lateinit var binding: FragmentAppointmentListBinding
    private lateinit var viewModel: HistoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAppointmentListBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity())[HistoryViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = requireActivity().getSharedPreferences("user_info", AppCompatActivity.MODE_PRIVATE)
        val userId = pref.getString("user_Id", "")
        val token = pref.getString("api_token", "")

        viewModel.getAppointments(token!!, userId!!)

        setUpView()
        setUpEvents()
    }

    private fun setUpEvents() {
        viewModel.selectedAppointmentNum.observe(requireActivity()) {
//            requireActivity().supportFragmentManager.beginTransaction()
//                .replace(R.id.fragment, AppointmentDetailFragment())
//                //.addToBackStack(null)
//                .commit()
            //binding.root.findNavController().navigate(R.id.action_appointmentListFragment_to_appointmentDetailFragment)
        }
    }

    private fun setUpView() {
        viewModel.appointmentsLiveData.observe(requireActivity()) {
            it?.let {
                binding.circularProgressBar.visibility = View.GONE
                if (it.isEmpty()) {
                    binding.noAppointments.visibility = View.VISIBLE
                }
                val adapter = AppointmentAdapter(requireContext(), viewModel, it)
                binding.recyclerviewAppointments.adapter = adapter
                binding.recyclerviewAppointments.layoutManager = LinearLayoutManager(requireContext())
            }
        }
    }

}