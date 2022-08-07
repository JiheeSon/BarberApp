package com.example.barberapp.view.appointment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.barberapp.R
import com.example.barberapp.databinding.ItemBarberBinding
import com.example.barberapp.model.Constants.BASE_IMAGE_URL
import com.example.barberapp.model.remote.response.barber.Barber
import com.example.barberapp.viewmodel.AppointmentViewModel

class BarberListAdapter(private val barberList: List<Barber>, private val viewModel: AppointmentViewModel): RecyclerView.Adapter<BarberListAdapter.BarberListViewHolder>() {
    private lateinit var binding: ItemBarberBinding
    private lateinit var itemClickListener: ItemClickListener

    interface ItemClickListener {
        fun onClick(view: View, position: Int)
    }

    fun setOnItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    inner class BarberListViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        fun bind(barber: Barber) {
            binding.apply {
                textName.text = barber.barberName
                textGender.text = if (barber.gender == "M") "Male" else "Female"
                val time = "Break Time: ${barber.breakTimeFrom} - ${barber.breakTimeTo}"
                textBreaktime.text = time
                textHoliday.text = barber.holiday
                ratingBar.rating = barber.userRating.toFloat()
                Glide.with(view.context)
                    .load(BASE_IMAGE_URL + barber.profilePic)
                    .placeholder(R.drawable.ic_baseline_person_24)
                    .into(imgBarber)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarberListViewHolder {
        binding = ItemBarberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BarberListViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: BarberListViewHolder, position: Int) {
        holder.bind(barberList[position])
        holder.itemView.setOnClickListener {
            viewModel.selectedBarberId.postValue(barberList[position].barberId)
        }
    }

    override fun getItemCount(): Int {
        return barberList.size
    }
}