package com.example.barberapp.view.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.barberapp.databinding.ItemNotificationBinding
import com.example.barberapp.model.remote.response.Alert

class AlertAdapter(private val alertList: List<Alert>): RecyclerView.Adapter<AlertAdapter.AlertViewHolder>() {
    private lateinit var binding: ItemNotificationBinding

    inner class AlertViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        fun bind(alert: Alert) {
            binding.apply {
                textMessage.text = alert.message
                textDate.text = alert.createdOn
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder {
        binding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlertViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
        holder.bind(alertList[position])
    }

    override fun getItemCount(): Int {
        return alertList.size
    }
}