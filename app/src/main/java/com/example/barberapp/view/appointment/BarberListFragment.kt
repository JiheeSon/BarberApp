package com.example.barberapp.view.appointment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.barberapp.databinding.FragmentBarberListBinding
import com.example.barberapp.model.remote.response.barber.Barber
import com.example.barberapp.viewmodel.AppointmentViewModel

class BarberListFragment : Fragment() {
    private lateinit var binding: FragmentBarberListBinding
    private lateinit var viewModel: AppointmentViewModel
    private lateinit var adapter: BarberListAdapter
    private lateinit var barberList: List<Barber>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBarberListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(AppointmentViewModel::class.java)
        setUpView()
        setUpEvent()
    }

    private fun setUpEvent() {
        viewModel.selectedBarberId.observe(requireActivity()) {
            Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun setUpView() {
        viewModel.barbersResponse.observe(requireActivity()) {
            adapter = BarberListAdapter(it.barbers, viewModel)
            binding.recyclerviewBarbers.layoutManager = GridLayoutManager(context, 2)
            binding.recyclerviewBarbers.adapter = adapter

            adapter.setOnItemClickListener(object : BarberListAdapter.ItemClickListener{
                override fun onClick(view: View, position: Int) {
                    Log.i("jihee", "hello")
                }
            })
            test()

        }
    }

    private fun test() {

    }

}