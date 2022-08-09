package com.example.barberapp.viewmodel

import android.util.Log
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barberapp.model.Repository
import com.example.barberapp.model.remote.response.barber.Barber
import com.example.barberapp.model.remote.response.barber.BarberServiceResponse
import com.example.barberapp.model.remote.response.barber.Service
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

class AppointmentViewModel(private val repository: Repository): ViewModel() {
    val barbersResponse = repository.barbersResponse
    val error: LiveData<String> = repository.error
    val isProcessing = repository.isProcessing

    val serviceError = MutableLiveData<String>()
    val processing = MutableLiveData<Boolean>()
    val barberServiceResponse = MutableLiveData<BarberServiceResponse>()

    val barberServiceCategories = MutableLiveData<ArrayList<String>>()
    val barberServiceItems = MutableLiveData<ArrayList<ArrayList<Service>>>()

    val selectedBarber = MutableLiveData<Barber>()
    val selectedServices = MutableLiveData<ArrayList<Service>>()
    val selectedServiceId = MutableLiveData<ArrayList<Int>>()
    val totalDuration = MutableLiveData<Double>()
    val totalCost = MutableLiveData<Double>()
    val appointmentDate = MutableLiveData<String>()
    val startTime = MutableLiveData<String>()
    val endTime = MutableLiveData<String>()


    val appointmentResponse = repository.appointmentResponse
    val appointmentError = repository.appointmentError
    val appointmentProcessing = repository.appointmentProcessing
    fun bookAppointment(ps_auth_token: String, params: HashMap<String, Any>) {
        repository.bookAppointment(ps_auth_token, params)
    }

    val appointmentsStartFromLiveData = MutableLiveData<Int>()
    val appointmentsDateLiveData = MutableLiveData<String>()
    val appointmentsSlotLiveData = MutableLiveData<Int>()
    val currentAppointmentsLiveData = repository.currentAppointmentsLiveData
    fun getCurrentAppointments() {
        repository.getCurrentAppointments(selectedBarber.value?.barberId.toString())
    }

    fun getBarberList() {
        repository.getBarbers()
        preloadData()
    }

    private fun preloadData() {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.IO) {
                getBarberServices()
            }

            launch(Dispatchers.IO) {
                setUpServiceData()
            }
        }
    }

    private fun setUpServiceData() {
        val services = barberServiceResponse.value?.services

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
//            Log.i("jihee", barberServiceCategories.value.toString())
//            Log.i("jihee", barberServiceItems.value.toString())
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

    fun onServiceCheckChange(isChecked: Boolean, selected: Service) {
        if (selectedServices.value == null) {
            selectedServices.value = ArrayList<Service>()
            selectedServiceId.value = ArrayList()
        }

        if (isChecked) {
            selectedServices.value!!.add(selected)
            selectedServiceId.value!!.add(selected.serviceId)
        } else {
            selectedServices.value!!.remove(selected)
            selectedServiceId.value!!.remove(selected.serviceId)
        }
    }

    fun computeTotalDurationAndCost() {
        val services = selectedServices.value
        var duration = 0.0
        var cost = 0.0
        if (services != null) {
            for (service in services) {
                duration += service.duration
                cost += service.cost
            }
        }
        totalDuration.postValue(duration)
        totalCost.postValue(cost)
        appointmentsSlotLiveData.postValue((duration / 15 + 0.5).roundToInt())
    }
}