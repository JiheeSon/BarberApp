package com.example.barberapp.view.service

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.barberapp.R
import com.example.barberapp.databinding.ItemServiceListBinding
import com.example.barberapp.model.Constants
import com.example.barberapp.model.remote.response.service.Service


class ServiceListAdapter(private val services: List<Service>) : RecyclerView.Adapter<ServiceListAdapter.ItemViewHolder>() {
    private lateinit var binding: ItemServiceListBinding
    private lateinit var itemClickListener: ItemClickListener

    interface ItemClickListener {
        fun onClick(view: View, position: Int)
    }

    fun setOnItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    inner class ItemViewHolder(val view: View): RecyclerView.ViewHolder(view) {

        fun bind(service: Service) {
            view.findViewById<TextView>(R.id.text_service_name)?.text = service.serviceName
            view.findViewById<TextView>(R.id.text_duration)?.text = service.duration.toInt().toString() + " Minutes"
            view.findViewById<TextView>(R.id.text_cost)?.text = "$ " + service.cost.toString()

            val img = view.findViewById<ImageView>(R.id.img_service)
            Glide.with(view.context)
                .load(Constants.BASE_IMAGE_URL + service.servicePic)
                .into(img!!)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        binding = ItemServiceListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(services[position])
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    override fun getItemCount(): Int {
        return services.size
    }
}