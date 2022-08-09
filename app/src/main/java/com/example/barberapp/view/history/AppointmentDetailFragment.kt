package com.example.barberapp.view.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.barberapp.R
import com.example.barberapp.databinding.FragmentAppointmentDetailBinding
import com.example.barberapp.model.Constants
import com.example.barberapp.view.appointment.adapter.SelectedServiceAdapter
import com.example.barberapp.viewmodel.HistoryViewModel

class AppointmentDetailFragment : Fragment() {
    private lateinit var binding: FragmentAppointmentDetailBinding
    private lateinit var viewModel: HistoryViewModel

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
    }

    private fun setUpView() {
        viewModel.appointmentLiveData.observe(requireActivity()) {
            binding.apply {
                textDate.text = it.aptDate
                textTime.text = "${it.timeFrom} to ${it.timeTo} (${it.totalDuration} Minutes)"
                textBarber.text = it.barberName
                textAppointmentId.text = "Appointment Number ${it.aptNo}"
                textStatus.text = it.aptStatus

                if (it.aptStatus == "Canceled") {
                    textStatus.setCompoundDrawables(requireContext().getDrawable(R.drawable.ic_baseline_cancel_24), null, null, null)
                    btnCancel.visibility = View.GONE
                    btnReschedule.visibility = View.GONE
                }

                Glide.with(requireContext())
                    .load(Constants.BASE_IMAGE_URL + it.profilePic)
                    .into(imgBarber)
            }

            val adapter = SelectedServiceAdapter(it.services)
            binding.recyclerviewServices.layoutManager = LinearLayoutManager(context)
            binding.recyclerviewServices.adapter = adapter
        }
    }

}