package com.example.barberapp.view.history

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.barberapp.R
import com.example.barberapp.databinding.FragmentAppointmentDetailBinding
import com.example.barberapp.model.Constants
import com.example.barberapp.model.remote.response.appointment.Appointment
import com.example.barberapp.view.appointment.adapter.SelectedServiceAdapter
import com.example.barberapp.viewmodel.HistoryViewModel

class AppointmentDetailFragment : Fragment() {
    private lateinit var binding: FragmentAppointmentDetailBinding
    private lateinit var viewModel: HistoryViewModel
    private lateinit var appointment: Appointment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAppointmentDetailBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[HistoryViewModel::class.java]
        viewModel.getAppointmentDetail()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpView()
        setUpEvents()
    }

    private fun setUpEvents() {
        binding.btnCancel.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
                .setIcon(R.drawable.ic_baseline_check_24)
                .setTitle("Confirm Cancel")
                .setMessage("Are your sure you want to cancel this appointment? Once cancelled, you will be no more able to claim for this appointment.")
                .setPositiveButton("YES") { _, _ ->
                    viewModel.cancelAppointment(appointment.aptNo)
                }
                .setNegativeButton("No") { _, _ -> }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(true)
            alertDialog.show()
        }

        binding.btnReschedule.setOnClickListener {
            val options = NavOptions.Builder().apply {
                setPopUpTo(R.id.rescheduleFragment, true, false)
            }.build()
            binding.root.findNavController().navigate(R.id.action_appointmentDetailFragment_to_rescheduleFragment, null, options)
        }
    }

    private fun setUpView() {
        viewModel.appointmentLiveData.observe(requireActivity()) {
            appointment = viewModel.appointmentLiveData.value!!
            binding.apply {
                textDate.text = it.aptDate
                textTime.text = "${it.timeFrom} to ${it.timeTo} (${it.totalDuration} Minutes)"
                textBarber.text = it.barberName
                textAppointmentId.text = "Appointment Number ${it.aptNo}"
                textStatus.text = it.aptStatus
                textTotalCost.text = it.totalCost.toString()

                when (it.aptStatus) {
                    "Canceled" -> {
                        btnCancel.visibility = View.GONE
                        btnReschedule.visibility = View.GONE
                        stamp.setImageResource(R.drawable.canceled)
                    }
                    "Rescheduled" -> {
                        stamp.setImageResource(R.drawable.rescheduled)
                    }
                    else -> {
                        stamp.setImageResource(R.drawable.confirmed)
                    }
                }

                if (isAdded) {
                    Glide.with(requireContext())
                        .load(Constants.BASE_IMAGE_URL + it.profilePic)
                        .into(imgBarber)
                }
            }

            val adapter = SelectedServiceAdapter(it.services)
            binding.recyclerviewServices.layoutManager = LinearLayoutManager(context)
            binding.recyclerviewServices.adapter = adapter
        }
    }

}