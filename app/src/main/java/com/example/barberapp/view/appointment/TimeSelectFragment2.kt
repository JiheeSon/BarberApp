package com.example.barberapp.view.appointment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.barberapp.R
import com.example.barberapp.databinding.FragmentTimeSelect2Binding
import com.example.barberapp.view.appointment.adapter.DateSlotAdapter
import com.example.barberapp.view.appointment.adapter.TimeSlotAdapter
import com.example.barberapp.viewmodel.AppointmentViewModel

class TimeSelectFragment2 : Fragment() {
    private lateinit var binding: FragmentTimeSelect2Binding
    private lateinit var viewModel: AppointmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTimeSelect2Binding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[AppointmentViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getCurrentAppointments()

        binding.recyclerviewDates.visibility = View.GONE

        viewModel.appointmentsStartFromLiveData.postValue(-1)

        setUpView()
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

        binding.btnContinue.setOnClickListener {
            if (viewModel.appointmentsStartFromLiveData.value!! == -1) {
                val builder = AlertDialog.Builder(requireContext())
                    .setTitle("Time Error")
                    .setMessage("Please select time.")
                    .setPositiveButton("Ok") { _, _ ->
                    }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(true)
                alertDialog.show()
            } else {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment, SummaryFragment())
                    //.addToBackStack(null)
                    .commit()
            }

            binding.btnCancel.setOnClickListener {
//            val action = BookSelectTimeFragmentDirections.bookCancelAction()
//            binding.root.findNavController().navigate(action)
            }

        }
    }

    private fun setUpView() {
        viewModel.currentAppointmentsLiveData.observe(requireActivity()) {
            val availableSlots = it.filter { it.slots.size > 0 }
            val dateAdapter = DateSlotAdapter(this, viewModel, availableSlots)
            binding.recyclerviewDates.adapter = dateAdapter
            binding.recyclerviewDates.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            binding.textSelectedDate.text = "${availableSlots[0].day}, ${availableSlots[0].date}"
            viewModel.appointmentsDateLiveData.postValue(availableSlots[0].date)
        }

        viewModel.appointmentsDateLiveData.observe(requireActivity()) { date ->
            viewModel.currentAppointmentsLiveData.value!!.forEach(){
                if(it.date == date){
                    binding.textSelectedDate.text = "${it.day}, ${it.date}"
                    val timeAdapter = TimeSlotAdapter(this, it.slots)
                    binding.recyclerviewTime.adapter = timeAdapter
                    binding.recyclerviewTime.layoutManager = GridLayoutManager(context, 4)
                }
            }
        }

        binding.textSelectSlots.text = "Select ${viewModel.appointmentsSlotLiveData.value} Slots"

    }
}