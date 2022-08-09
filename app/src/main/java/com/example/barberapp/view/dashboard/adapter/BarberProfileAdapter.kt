package com.example.barberapp.view.dashboard.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.barberapp.R
import com.example.barberapp.databinding.ItemBarberProfileBinding
import com.example.barberapp.model.Constants
import com.example.barberapp.model.remote.response.barber.Barber

class BarberProfileAdapter (private val barberList: List<Barber>): RecyclerView.Adapter<BarberProfileAdapter.BarberProfileViewHolder>() {
    private lateinit var binding: ItemBarberProfileBinding

    inner class BarberProfileViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        fun bind(barber: Barber) {
            binding.apply {
                textName.text = barber.barberName
                ratingBar.rating = barber.userRating.toFloat()
                Glide.with(view.context)
                    .load(Constants.BASE_IMAGE_URL + barber.profilePic)
                    .placeholder(R.drawable.ic_baseline_person_24)
                    .into(imgBarber)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarberProfileViewHolder {
        binding = ItemBarberProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BarberProfileViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: BarberProfileViewHolder, position: Int) {
        holder.bind(barberList[position])
    }

    override fun getItemCount(): Int {
        return barberList.size
    }
}