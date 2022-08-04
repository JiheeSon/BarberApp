package com.example.barberapp.view.appointment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.barberapp.R
import com.example.barberapp.view.dashboard.DashboardActivity

class BarberServiceFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        initToolBar()
        return inflater.inflate(R.layout.fragment_barber_service, container, false)
    }

    private fun initToolBar() {
        val toolbar = (activity as AppointmentActivity).supportActionBar
        toolbar?.apply {
            setTitle("Select Service")
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        }
    }

}