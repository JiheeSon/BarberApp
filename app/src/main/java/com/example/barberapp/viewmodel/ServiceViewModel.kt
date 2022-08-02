package com.example.barberapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.barberapp.model.Repository
import com.example.barberapp.model.remote.response.service.Service
import kotlinx.coroutines.launch

class ServiceViewModel(private val repository: Repository): ViewModel() {
    val serviceCategoryResponse = repository.serviceCategoryResponse
    val serviceResponse = repository.serviceResponse
    val services = MutableLiveData<ArrayList<List<Service>>>()
    val error: MutableLiveData<String> = repository.error
    val isProcessing = repository.isProcessing

    fun getService() {
        repository.getServiceCategory()

        val num = serviceCategoryResponse.value?.serviceCategories?.size
        services.value = ArrayList()
//        viewModelScope.launch {
//            for (i in 1..num!!) {
//                try {
//                    repository.getServiceByCategory(i.toString())
//                    services.value!!.add(serviceResponse.value?.services!!)
//                } catch (exception: Exception) {
//                    error.postValue(exception.message)
//                    isProcessing.set(false)
//                }
//            }
//            isProcessing.set(false)
//        }
    }
}