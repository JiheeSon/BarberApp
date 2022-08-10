package com.example.barberapp.view.dashboard.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.barberapp.R
import com.example.barberapp.databinding.ItemWorkingHourBinding
import com.example.barberapp.model.remote.response.workinghours.Weekday
import com.example.barberapp.viewmodel.DashboardViewModel

class WorkingHourAdapter(private val fragment: Fragment, private val viewModel: DashboardViewModel, private val hoursMap: Map<String, Weekday>) : RecyclerView.Adapter<WorkingHourAdapter.WorkingHourViewHolder>() {
    private lateinit var binding: ItemWorkingHourBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkingHourViewHolder {
        binding = ItemWorkingHourBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WorkingHourViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: WorkingHourViewHolder, position: Int) {
        holder.apply {
            val day = hoursMap.keys.toList()[position]
            val weekday = hoursMap.get(day)
            holder.bind(day, weekday!!)
            if (position == 0){
                binding.textDate.setTextColor(fragment.getResources().getColor(R.color.teal_700))
                binding.textTime.setTextColor(fragment.getResources().getColor(R.color.teal_700))
            }
        }
    }

    override fun getItemCount(): Int {
        return hoursMap.size
    }


    inner class WorkingHourViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(day: String, weekday: Weekday) {
            var time = "${weekday.fromTime} to ${weekday.toTime}"
            if(weekday.fromTime == weekday.toTime){
                time = "Holiday"
                binding.textTime.setTextColor(fragment.getResources().getColor(R.color.main_red))
            }
            binding.textDate.text = day
            binding.textTime.text = time

        }
    }
}
