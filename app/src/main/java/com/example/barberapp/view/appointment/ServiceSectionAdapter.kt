package com.example.barberapp.view.appointment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.barberapp.R
import com.example.barberapp.databinding.ItemBarberServiceSectionBinding
import com.example.barberapp.model.remote.response.barber.Service

class ServiceSectionAdapter(private val categories: ArrayList<String>, private val services: ArrayList<ArrayList<Service>>): RecyclerView.Adapter<ServiceSectionAdapter.ServiceSectionViewHolder>() {
    private lateinit var binding: ItemBarberServiceSectionBinding

    inner class ServiceSectionViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        fun bind(idx: Int) {
            view.findViewById<TextView>(R.id.text_category).text = categories[idx]
            //binding.textCategory.text = categories[idx]
            val adapter = ServiceItemAdapter(view.context, services[idx])
            view.findViewById<ListView>(R.id.listview).adapter = adapter
            //binding.listview.adapter = adapter
            setListViewHeight(binding.listview)
        }
    }

    private fun setListViewHeight(listview: ListView) {
        val adapter = listview.adapter
        var height = 0
        for (i in 0 until adapter.count-1) {
            val item = adapter.getView(i, null, listview)
            item.measure(0,0)
            height += item.measuredHeight
        }
        val params : ViewGroup.LayoutParams = listview.layoutParams
        params.height = height + listview.dividerHeight * (adapter.count - 1)
        listview.layoutParams = params
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceSectionViewHolder {
        binding = ItemBarberServiceSectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ServiceSectionViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ServiceSectionViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return services.size
    }
}