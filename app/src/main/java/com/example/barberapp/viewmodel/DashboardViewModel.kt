package com.example.barberapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barberapp.model.Repository
import com.example.barberapp.model.remote.response.AlertResponse
import com.example.barberapp.model.remote.response.DashboardResponse
import com.example.barberapp.model.remote.response.LoginResponse
import com.example.barberapp.model.remote.response.barber.BarbersResponse
import com.example.barberapp.model.remote.response.contacts.Contact
import com.example.barberapp.model.remote.response.service.ServiceCategoryResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DashboardViewModel(private val repository: Repository): ViewModel() {

    val error: LiveData<String> = repository.error
    val isProcessing = repository.isProcessing
    val dashboardResponse: LiveData<DashboardResponse> = repository.dashboardResponse
    val barbersResponse: LiveData<BarbersResponse> = repository.barbersResponse
    val categoryResponse: LiveData<ServiceCategoryResponse> = repository.serviceCategoryResponse

    val notiError = MutableLiveData<String>()
    val processing = MutableLiveData<Boolean>()
    val alertLiveData = MutableLiveData<AlertResponse>()

    fun getDashboard() {
        repository.getDashboard()
        repository.getBarbers()
        repository.getServiceCategory()
        preloadData()
    }

    private fun preloadData() {
        viewModelScope.launch(Dispatchers.IO)
        {
            try {
                processing.postValue(true)
                val response = repository.getAlert()
                processing.postValue(false)
                if (!response.isSuccessful) {
                    notiError.postValue("Failed to load data.Error code: ${response.code()}")
                    return@launch
                }
                response.body()?.let {
                    alertLiveData.postValue(it)
                }
            }catch (e: Exception) {
                notiError.postValue("Error is : $e")
                e.printStackTrace()
                processing.postValue(false)
            }
        }
    }

    val contactsLiveData = MutableLiveData<ArrayList<Contact>>()
    fun getContacts() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getContacts()
                if (!response.isSuccessful) {
                    return@launch
                }
                response.body()?.let {
                    val contacts: ArrayList<Contact> = it.contacts as ArrayList<Contact>
                    contacts.sortBy { it.displayOrder }
                    contactsLiveData.postValue(contacts)
                }
            } catch (e: Exception) {
                Log.e("Exception", e.toString())
                e.printStackTrace()
            }
        }
    }
}