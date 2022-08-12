package com.example.barberapp.view.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.barberapp.R
import com.example.barberapp.databinding.FragmentRescheduleBinding
import com.example.barberapp.view.appointment.adapter.DateSlotAdapter
import com.example.barberapp.view.appointment.adapter.TimeSlotAdapter
import com.example.barberapp.viewmodel.HistoryViewModel

class RescheduleFragment : Fragment() {
    private lateinit var binding: FragmentRescheduleBinding
    private lateinit var viewModel: HistoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRescheduleBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[HistoryViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCurrentAppointments()
        binding.recyclerviewDates.visibility = View.GONE
        viewModel.appointmentsStartFromLiveData.postValue(-1)

        setUpViews()
        setUpEvents()
    }

    private fun setUpEvents() {
        binding.btnDropDown.isSelected = false
        binding.btnDropDown.setOnClickListener {
            binding.btnDropDown.isSelected = !binding.btnDropDown.isSelected
            if (binding.btnDropDown.isSelected) {
                binding.recyclerviewDates.visibility = View.VISIBLE
                binding.btnDropDown.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
            } else {
                binding.recyclerviewDates.visibility = View.GONE
                binding.btnDropDown.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }

        binding.btnConfirm.setOnClickListener {
            rescheduleAppointment()
        }
    }

    private fun rescheduleAppointment() {
        if (viewModel.appointmentsStartFromLiveData.value == -1) {
            val builder = AlertDialog.Builder(requireContext())
                .setIcon(R.drawable.ic_baseline_error_24)
                .setTitle("Error!")
                .setMessage("Please select time.")
                .setPositiveButton("Ok") { _, _ ->
                }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(true)
            alertDialog.show()
        } else {
            makeApiCall()
            afterApiCall()
            //binding.root.findNavController().navigate(R.id.action_rescheduleFragment_to_appointmentDetailFragment)
        }

    }

    private fun afterApiCall() {
        viewModel.rescheduleError.observe(requireActivity()) {
            val builder = AlertDialog.Builder(requireContext())
                .setIcon(R.drawable.ic_baseline_error_24)
                .setTitle("Error!")
                .setMessage(it)
                .setPositiveButton("Ok") { _, _ ->
                    //binding.root.findNavController().navigate(R.id.action_rescheduleFragment_to_appointmentDetailFragment)
                }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(true)
            alertDialog.show()
        }
        viewModel.appointmentLiveData.observe(requireActivity()) {
            val builder = AlertDialog.Builder(requireContext())
                .setIcon(R.drawable.ic_baseline_check_24)
                .setTitle("Thank you")
                .setMessage("Your appointment has been rescheduled!")
                .setPositiveButton("Ok") { dialog, _ ->
                    dialog.dismiss()
                    binding.root.findNavController().navigate(R.id.action_rescheduleFragment_to_appointmentDetailFragment)
                }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(true)
            alertDialog.show()
        }
    }

    private fun makeApiCall() {
        var fromTimeString = ""
        var toTimeString = ""
        val timeFrom = viewModel.appointmentsStartFromLiveData.value!!
        val slot = viewModel.appointmentsSlotLiveData.value!!
        val date = viewModel.appointmentsDateLiveData.value!!
        val currentAppointments = viewModel.currentAppointmentsLiveData.value!!
        currentAppointments.forEach() {
            if (it.date == date) {
                fromTimeString = it.slots.keys.elementAt(timeFrom).split("-")[0]
                toTimeString = it.slots.keys.elementAt(timeFrom + slot - 1).split("-")[1]
            }
        }
        val map = HashMap<String, Any>()
        map["aptNo"] = viewModel.appointmentLiveData.value!!.aptNo
        map["timeFrom"] = fromTimeString
        map["timeTo"] = toTimeString
        map["aptDate"] = date
        viewModel.rescheduleAppointment(map)

    }

    private fun setUpViews() {
        viewModel.currentAppointmentsLiveData.observe(requireActivity()) {
            val availableSlots = it.filter { it.slots.size > 0 }
            val dateAdapter = DateSlotAdapter(this, viewModel.appointmentsDateLiveData, availableSlots)
            binding.recyclerviewDates.adapter = dateAdapter
            binding.recyclerviewDates.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            binding.textSelectedDate.text = "${availableSlots[0].day}, ${availableSlots[0].date}"
            viewModel.appointmentsDateLiveData.postValue(availableSlots[0].date)
        }

        viewModel.appointmentsDateLiveData.observe(requireActivity()) { date ->
            viewModel.currentAppointmentsLiveData.value!!.forEach(){
                if(it.date == date){
                    binding.textSelectedDate.text = "${it.day}, ${it.date}"
                    val timeAdapter = TimeSlotAdapter(this, it.slots, viewModel.appointmentsStartFromLiveData, viewModel.appointmentsSlotLiveData)
                    binding.recyclerviewTime.adapter = timeAdapter
                    binding.recyclerviewTime.layoutManager = GridLayoutManager(context, 4)
                }
            }
        }

        binding.textSelectSlots.text = "Select ${viewModel.appointmentsSlotLiveData.value} Slots"

    }
}