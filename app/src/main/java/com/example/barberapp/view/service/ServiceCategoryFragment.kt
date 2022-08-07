package com.example.barberapp.view.service

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.barberapp.R
import com.example.barberapp.databinding.FragmentServiceCategoryBinding
import com.example.barberapp.view.appointment.AppointmentActivity
import com.example.barberapp.view.dashboard.CategoryAdapter
import com.example.barberapp.viewmodel.ServiceViewModel

class ServiceCategoryFragment : Fragment() {
    private lateinit var binding: FragmentServiceCategoryBinding
    private lateinit var viewModel: ServiceViewModel
    private lateinit var adapter: ServiceCategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentServiceCategoryBinding.inflate(layoutInflater, container, false)
        (activity as ServiceActivity).supportActionBar?.setTitle("Services")
        viewModel = ViewModelProvider(requireActivity())[ServiceViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpView()
    }

    private fun setUpView() {
        viewModel.serviceCategoryResponse.observe(requireActivity()) {
            adapter = ServiceCategoryAdapter(it.serviceCategories)
            binding.recyclerviewCategory.layoutManager = LinearLayoutManager(context)
            binding.recyclerviewCategory.adapter = adapter

            adapter.setOnItemClickListener(object : ServiceCategoryAdapter.ItemClickListener{
                override fun onClick(view: View, position: Int) {
                    viewModel.getServiceByCategory(it.serviceCategories[position].categoryId)
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment, ServiceListFragment())
                        .addToBackStack(null)
                        .commit()
                }
            })
        }
    }

}