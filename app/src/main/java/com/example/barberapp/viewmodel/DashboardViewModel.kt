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
import com.example.barberapp.model.remote.response.workinghours.Weekday
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

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

    var compositeDisposable: CompositeDisposable = CompositeDisposable()
    val workingHourLiveData = MutableLiveData<Map<String, Weekday>>()
    fun getWorkingHour() {
        compositeDisposable.add(repository.getWorkingHours()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    val workingHour = it.timings.entries.sortedBy { getWeekOfDate(it.key) }.associateBy({ it.key }, { it.value })
                    workingHourLiveData.postValue(workingHour)
                },
                { t: Throwable? -> Log.i("Throwable", t?.message ?: "error") }
            )

        )
    }

    private fun getWeekOfDate(day: String): Int {
        val weekDays = arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
        val intDay = weekDays.indexOf(day)
        val cal = Calendar.getInstance()
        var w = cal[Calendar.DAY_OF_WEEK] - 1
        println((intDay + 7 - w) % 7)
        if (w < 0) {
            w = 0
        }
        return (intDay + 7 - w) % 7
    }
}