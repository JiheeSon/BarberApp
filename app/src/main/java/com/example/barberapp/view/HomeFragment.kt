package com.example.barberapp.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.barberapp.databinding.FragmentHomeBinding
import com.example.barberapp.view.service.ServiceActivity

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpEvents()
    }

    private fun setUpEvents() {
        binding.apply {
            btnReserve.setOnClickListener {  }
            btnService.setOnClickListener { startActivity(Intent(context, ServiceActivity::class.java)) }
            btnHours.setOnClickListener {  }
            btnMore.setOnClickListener {  }
        }
    }

}