package com.example.barberapp.view.appointment.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.example.barberapp.R
import com.example.barberapp.databinding.FragmentTimeSelectBinding
import com.example.barberapp.viewmodel.AppointmentViewModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class TimeSelectFragment : Fragment() {
    private lateinit var binding: FragmentTimeSelectBinding
    private lateinit var viewModel: AppointmentViewModel
    private lateinit var appointmentDate: String
    private lateinit var startTime: String
    private lateinit var endTime: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTimeSelectBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[AppointmentViewModel::class.java]
        //binding.fragment = this
        setUpView()
        setUpEvent()

    }

    private fun setUpEvent() {
        binding.btnDate.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setCalendarConstraints(CalendarConstraints.Builder().setValidator(DateValidatorPointForward.now()).build())
                .build()

            datePicker.addOnPositiveButtonClickListener {
                val calendar = Calendar.getInstance()
                calendar.time = Date(it)
                val date = calendar.timeInMillis
                appointmentDate = "${calendar.get(Calendar.MONTH) + 1}/${calendar.get(Calendar.DAY_OF_MONTH) + 1}/${calendar.get(Calendar.YEAR)}"
                binding.textDate.text = appointmentDate
            }

            datePicker.show(requireActivity().supportFragmentManager, "tag")
        }

        binding.btnTime.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(8)
                .setMinute(0)
                .setTitleText("Select time")
                .build()

            timePicker.addOnPositiveButtonClickListener {
                startTime = "${timePicker.hour} : ${zero(timePicker.minute)}${timePicker.minute}"
                val text = "$startTime - ${getEndTimeText(timePicker)}"
                binding.textTime.text = text
            }
            timePicker.show(requireActivity().supportFragmentManager, "tag")
        }

        binding.btnContinue.setOnClickListener {
            onContinueClick()
        }
    }

    private fun onContinueClick() {
        if (this::appointmentDate.isInitialized && this::startTime.isInitialized && this::endTime.isInitialized) {
            viewModel.appointmentDate.postValue(appointmentDate)
            viewModel.startTime.postValue(startTime)
            viewModel.endTime.postValue(endTime)

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment, SummaryFragment())
                //.addToBackStack(null)
                .commit()
        } else {
            if (!this::appointmentDate.isInitialized) {
                Snackbar.make(binding.root, "Please select appointment date", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Confirm") {}
                    .show()
            } else if (!this::startTime.isInitialized) {
                Snackbar.make(binding.root, "Please select appointment time", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Confirm") {}
                    .show()
            }
        }
    }

    private fun getEndTimeText(timePicker: MaterialTimePicker): String {
        var min = timePicker.minute + viewModel.totalDuration.value?.toInt()!!
        var hour = timePicker.hour
        if (min > 60) {
            hour += min / 60
            min %= 60
        }
        endTime = "$hour : ${zero(min)}$min"
        return endTime
    }

    private fun zero(minute: Int): String {
        return if (minute < 10) "0" else ""
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUpView() {
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("MMM dd yyyy")
        binding.textToday.text = "Today: ${today.format(formatter)}"

        viewModel.totalDuration.observe(requireActivity()) {
            val message = "* Your appointment will be ${it.toInt()} minutes long"
            binding.textDuration.text = message
        }
    }
}