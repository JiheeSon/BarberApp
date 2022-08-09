package com.example.barberapp.view.appointment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.barberapp.R
import com.example.barberapp.databinding.ItemSelectTimeSlotBinding
import com.example.barberapp.viewmodel.AppointmentViewModel

class TimeSlotAdapter (private val fragment: Fragment, val infoMap: Map<String, Boolean>) : RecyclerView.Adapter<TimeSlotAdapter.TimeSlotViewHolder>() {
    private lateinit var mainViewModel: AppointmentViewModel
    private lateinit var binding: ItemSelectTimeSlotBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeSlotViewHolder {
        binding = ItemSelectTimeSlotBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        mainViewModel = ViewModelProvider(fragment.requireActivity())[AppointmentViewModel::class.java]
        return TimeSlotViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TimeSlotViewHolder, position: Int) {
        holder.apply {
            val info = infoMap.keys.elementAt(position)
            holder.bind(info, infoMap[info]!!, position)
        }
    }

    override fun getItemCount(): Int {
        return infoMap.size
    }

    fun freeSlots(slots: Int, position: Int): Int {
        for (i in 0 until slots) {
            if (position + i >= infoMap.size || infoMap[infoMap.keys.elementAt(position + i)] == true) {
                return i
            }
        }
        return -1
    }


    inner class TimeSlotViewHolder(val binding: ItemSelectTimeSlotBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(time: String, booked: Boolean, position: Int) {
            binding.tvTimeSlot.text = time.split("-")[0]
            if (booked) {
                binding.tvTimeSlot.setBackgroundResource(R.drawable.time_slot_booked)
            } else {
                binding.tvTimeSlot.setBackgroundResource(R.drawable.time_slot_available)
            }
            binding.tvTimeSlot.setOnClickListener {
                val slots = mainViewModel.appointmentsSlotLiveData.value!!
                val freeSlots = freeSlots(slots, position)
                if (freeSlots == -1) {
                    mainViewModel.appointmentsStartFromLiveData.postValue(position)
                } else {
                    val builder = AlertDialog.Builder(fragment.requireContext())
                        .setTitle("Sorry")
                        .setIcon(R.drawable.ic_baseline_error_24)
                        .setMessage("Total required slots are $slots. From current selected position only $freeSlots are available.")
                        .setPositiveButton("Ok") { _, _ ->
                        }
                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.setCancelable(true)
                    alertDialog.show()
                }
            }

            mainViewModel.appointmentsStartFromLiveData.observe(fragment.requireActivity()) {
                val slots = mainViewModel.appointmentsSlotLiveData.value!!
                if (it != -1 && position in it until (it + slots)) {
                    binding.tvTimeSlot.setBackgroundResource(R.drawable.time_slot_selected)
                } else {
                    if (booked) {
                        binding.tvTimeSlot.setBackgroundResource(R.drawable.time_slot_booked)
                    } else {
                        binding.tvTimeSlot.setBackgroundResource(R.drawable.time_slot_available)
                    }
                }
            }

        }
    }
}
