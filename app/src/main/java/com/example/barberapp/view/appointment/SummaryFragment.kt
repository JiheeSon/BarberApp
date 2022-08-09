package com.example.barberapp.view.appointment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.barberapp.R
import com.example.barberapp.databinding.FragmentSummaryBinding
import com.example.barberapp.model.Constants.BASE_IMAGE_URL
import com.example.barberapp.view.appointment.adapter.SelectedServiceAdapter
import com.example.barberapp.viewmodel.AppointmentViewModel

class SummaryFragment : Fragment() {
    private lateinit var binding: FragmentSummaryBinding
    private lateinit var viewModel: AppointmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSummaryBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[AppointmentViewModel::class.java]
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = requireActivity().getSharedPreferences("user_info", AppCompatActivity.MODE_PRIVATE)
        val userId = pref.getString("user_Id", "")
        val token = pref.getString("api_token", "")
        setUpView()
        setUpEvent(userId, token!!)
    }

    private fun setUpEvent(userId: String?, token: String) {
        val date = viewModel.appointmentsDateLiveData.value!!
        val timeFrom = viewModel.appointmentsStartFromLiveData.value!!
        val slot = viewModel.appointmentsSlotLiveData.value!!
        val currentAppointments = viewModel.currentAppointmentsLiveData.value!!

        var fromTimeString = ""
        var toTimeString = ""
        currentAppointments.forEach() {
            if (it.date == date) {
                fromTimeString = it.slots.keys.elementAt(timeFrom).split("-")[0]
                toTimeString = it.slots.keys.elementAt(timeFrom + slot - 1).split("-")[1]
            }
        }
        val selectedTime = "$fromTimeString-$toTimeString (${viewModel.totalDuration.value.toString()} Minutes)"
        binding.textTime.text = selectedTime

        binding.btnContinue.setOnClickListener {
            val params = HashMap<String, Any>()
            params["userId"] = userId.toString()
            params["barberId"] = viewModel.selectedBarber.value?.barberId.toString()
            params["services"] = viewModel.selectedServiceId.value.toString()
            params["aptDate"] = viewModel.appointmentsDateLiveData.value!!//viewModel.appointmentDate.value.toString()
            params["timeFrom"] = fromTimeString//viewModel.startTime.value.toString()
            params["timeTo"] = toTimeString//viewModel.endTime.value.toString()
            params["totalDuration"] = viewModel.totalDuration.value.toString()
            params["totalCost"] = viewModel.totalCost.value.toString()
            params["couponCode"] = ""
            params["sendSms"] = false

            viewModel.bookAppointment(token, params)

            viewModel.appointmentProcessing.observe(requireActivity()) {
                if (!it!!) {
                    openDialog()
                }
            }
        }
    }

    private fun openDialog() {
        viewModel.appointmentResponse.observe(requireActivity()) {
            val builder = AlertDialog.Builder(requireContext())
                .setIcon(R.drawable.ic_baseline_check_circle_24)
                .setTitle("Thank you!")
                .setMessage("Your appointment on ${it.appointment.aptDate} is booked successfully.")
                .setPositiveButton("Go to Home") { dialog, _ -> requireActivity().finish(); dialog.dismiss()}
            val dialog = builder.create()
            dialog.setCancelable(false)
            dialog.show()
        }

        viewModel.appointmentError.observe(requireActivity()) {
            val builder = AlertDialog.Builder(requireContext())
                .setIcon(R.drawable.ic_baseline_error_24)
                .setTitle("Error")
                .setMessage("$it\n\nPlease try again")
                .setPositiveButton("Go to Home") { dialog, _ -> requireActivity().finish(); dialog.dismiss()}
            val dialog = builder.create()
            dialog.setCancelable(false)
            dialog.show()
        }
    }

    private fun setUpView() {
        viewModel.selectedBarber.observe(requireActivity()) {
            Glide.with(requireContext())
                .load(BASE_IMAGE_URL + it.profilePic)
                .into(binding.imgBarber)
        }

        viewModel.selectedServices.observe(requireActivity()) {
            val adapter = SelectedServiceAdapter(it)
            binding.recyclerviewServices.layoutManager = LinearLayoutManager(context)
            binding.recyclerviewServices.adapter = adapter
        }
    }
}