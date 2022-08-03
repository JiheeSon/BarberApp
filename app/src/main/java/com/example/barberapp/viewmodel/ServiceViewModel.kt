package com.example.barberapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barberapp.model.Repository
import com.example.barberapp.model.remote.response.service.Service
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ServiceViewModel(private val repository: Repository): ViewModel() {
    val serviceCategoryResponse = repository.serviceCategoryResponse
    val services = MutableLiveData<ArrayList<List<Service>>>()
    val error: MutableLiveData<String> = repository.error
    val isProcessing = repository.isProcessing

    fun getService() {
        repository.getServiceCategory()
        preloadData()
    }

    private fun preloadData() {
        val num = serviceCategoryResponse.value?.serviceCategories?.size
        val list = ArrayList<List<Service>>()

//        for (i in 0 until num!!) {
//            viewModelScope.launch(Dispatchers.IO) {
//                try {
//                    val response = repository.getServiceByCategory()
//                } catch (e: Exception) {
//
//                }
//            }
//        }
    }
}