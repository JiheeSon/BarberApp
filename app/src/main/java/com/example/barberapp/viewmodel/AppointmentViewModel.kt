package com.example.barberapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barberapp.model.Repository
import com.example.barberapp.model.remote.response.barber.BarberServiceResponse
import com.example.barberapp.model.remote.response.barber.Service
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppointmentViewModel(private val repository: Repository): ViewModel() {
    val barbersResponse = repository.barbersResponse
    val error: LiveData<String> = repository.error
    val isProcessing = repository.isProcessing

    val serviceError = MutableLiveData<String>()
    val processing = MutableLiveData<Boolean>()
    val barberServiceResponse = MutableLiveData<BarberServiceResponse>()

    val barberServiceCategories = MutableLiveData<ArrayList<String>>()
    val barberServiceItems = MutableLiveData<ArrayList<ArrayList<Service>>>()
    val barberServicesByCategory = MutableLiveData<HashMap<String, ArrayList<Service>>>()

    val selectedBarberId = MutableLiveData<Int>()

    fun getBarberList() {
        repository.getBarbers()
        preloadData()
    }

    private fun preloadData() {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.IO) {
                getBarberServices()
            }

            withContext(Dispatchers.IO) {
                setUpServiceData()
            }
        }
    }

    private fun setUpServiceData() {
        val services = barberServiceResponse.value?.services
        val map: HashMap<String, ArrayList<Service>> = HashMap()

        val categories = ArrayList<String>()
        val items = ArrayList<ArrayList<Service>>()

        if (services != null) {
            for (service in services) {
//                if (!map.containsKey(service.serviceType)) {
//                    map.put(service.serviceType, arrayListOf(service))
//                } else {
//                    val tmp = map.get(service.serviceType)
//                    tmp?.add(service)
//                    map.put(service.serviceType, tmp!!)
//                }
                if (!categories.contains(service.serviceType)) {
                    categories.add(service.serviceType)
                    items.add(ArrayList<Service>().apply { add(service) })
                } else {
                    val idx = categories.indexOf(service.serviceType)
                    items[idx].add(service)
                }
            }
            //barberServicesByCategory.postValue(map)
            barberServiceCategories.postValue(categories)
            barberServiceItems.postValue(items)
        }
    }

    private suspend fun getBarberServices() {
        try {
            processing.postValue(true)
            val response = repository.getBarberServices()
            processing.postValue(false)
            if (!response.isSuccessful) {
                serviceError.postValue("Failed to load data. Error code: ${response.code()}")
            }
            response.body()?.let {
                barberServiceResponse.postValue(it)
            }
        }catch (e: Exception) {
            serviceError.postValue("Error is : $e")
            e.printStackTrace()
            processing.postValue(false)
        }
    }
}