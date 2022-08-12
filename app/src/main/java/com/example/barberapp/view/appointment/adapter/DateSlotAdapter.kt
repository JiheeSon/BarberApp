package com.example.barberapp.view.appointment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.barberapp.R
import com.example.barberapp.databinding.ItemSelectDateBinding
import com.example.barberapp.model.remote.response.appointment.Slot
import com.example.barberapp.viewmodel.AppointmentViewModel

class DateSlotAdapter(private val fragment: Fragment, private val appointmentsDateLiveData: MutableLiveData<String>, val slotList: List<Slot>) : RecyclerView.Adapter<DateSlotAdapter.DateSlotViewHolder>() {
    private lateinit var binding: ItemSelectDateBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateSlotViewHolder {
        binding = ItemSelectDateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DateSlotViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DateSlotViewHolder, position: Int) {
        holder.apply {
            val slot = slotList.get(position)
            holder.bind(slot)
        }
    }

    override fun getItemCount(): Int {
        return slotList.size
    }


    inner class DateSlotViewHolder(val binding: ItemSelectDateBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(slot: Slot) {
            binding.textDate.text = slot.date
            binding.textDay.text = slot.day
            appointmentsDateLiveData.observe(fragment.requireActivity()) {
                if (slot.date == it) {
                    binding.layout.setBackgroundColor(
                        ContextCompat.getColor(
                            fragment.requireContext(),
                            R.color.main_sky_blue
                        )
                    )
                } else {
                    binding.layout.setBackgroundColor(
                        ContextCompat.getColor(
                            fragment.requireContext(),
                            R.color.transparent
                        )
                    )
                }
            }
            binding.root.setOnClickListener {
                appointmentsDateLiveData.postValue(slot.date)
            }
        }
    }
}
