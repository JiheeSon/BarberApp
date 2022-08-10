package com.example.barberapp.view.history

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.barberapp.R
import com.example.barberapp.databinding.ItemAppointmentBinding
import com.example.barberapp.model.remote.response.history.AppointmentInfo
import com.example.barberapp.viewmodel.HistoryViewModel

class AppointmentAdapter (private val context: Context, private val viewModel: HistoryViewModel, val infoArrayList: List<AppointmentInfo>) : RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val binding = ItemAppointmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AppointmentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        holder.apply {
            val info = infoArrayList.get(position)
            holder.bind(info)
            holder.itemView.setOnClickListener {
                viewModel.selectedAppointmentNum.postValue(info.aptNo.toString())
            }
        }
    }

    override fun getItemCount(): Int {
        return infoArrayList.size
    }


    inner class AppointmentViewHolder(val binding: ItemAppointmentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(appointmentInfo: AppointmentInfo) {
            binding.apply {
                textAppointmentId.text = "Appointment # ${appointmentInfo.aptNo}"
                textDate.text = appointmentInfo.aptDate
                textTime.text = "${appointmentInfo.timeFrom} to ${appointmentInfo.timeTo} (${appointmentInfo.totalDuration} Minutes)"
                textStatus.text = appointmentInfo.aptStatus
                if (appointmentInfo.aptStatus == "Canceled") {
                    textStatus.setCompoundDrawables(ResourcesCompat.getDrawable(context.resources, R.drawable.ic_baseline_cancel_24, null), null, null, null)
                }
            }
        }
    }
}
