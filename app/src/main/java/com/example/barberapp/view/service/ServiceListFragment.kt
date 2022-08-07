package com.example.barberapp.view.service

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.barberapp.databinding.DialogServiceDetailBinding
import com.example.barberapp.databinding.FragmentServiceListBinding
import com.example.barberapp.model.Constants
import com.example.barberapp.model.remote.response.service.Service
import com.example.barberapp.viewmodel.ServiceViewModel

class ServiceListFragment : Fragment() {
    private lateinit var binding: FragmentServiceListBinding
    private lateinit var viewModel: ServiceViewModel
    private lateinit var observer: Observer<List<Service>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentServiceListBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[ServiceViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observer = Observer<List<Service>> {
            val adapter = ServiceListAdapter(it)
            binding.recyclerviewServices.layoutManager = LinearLayoutManager(context)
            binding.recyclerviewServices.adapter = adapter

            adapter.setOnItemClickListener(object : ServiceListAdapter.ItemClickListener {
                override fun onClick(view: View, position: Int) {
                    openDialog(it[position])
                }
            })
        }

        viewModel.services.observe(requireActivity(), observer)
    }

    private fun openDialog(service: Service) {
        val dialog = DialogServiceDetailBinding.inflate(layoutInflater)
        dialog.apply {
            textTitle.text = service.serviceName
            textDuration.text = service.duration.toInt().toString() + " Minutes"
            textRate.text = "$ " + service.cost
            textDescription.text = service.description
            Glide.with(requireContext())
                .load(Constants.BASE_IMAGE_URL + service.servicePic)

                .into(imgBackground)
            Glide.with(requireContext())
                .load(Constants.BASE_IMAGE_URL + service.servicePic)
                .into(imgMain)
        }

        val builder = AlertDialog.Builder(requireContext())
            .setView(dialog.root)
        val alertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
        dialog.btnClose.setOnClickListener {
            alertDialog.dismiss()
        }
    }
}