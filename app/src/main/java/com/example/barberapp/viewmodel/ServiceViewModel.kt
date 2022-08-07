package com.example.barberapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barberapp.model.Repository
import com.example.barberapp.model.remote.response.service.Service
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ServiceViewModel(private val repository: Repository): ViewModel() {
    val serviceCategoryResponse = repository.serviceCategoryResponse
    val services = MutableLiveData<List<Service>>()
    val error: MutableLiveData<String> = repository.error
    val isProcessing = repository.isProcessing

    val serviceError = MutableLiveData<String>()
    val processing = MutableLiveData<Boolean>()

    fun getServiceByCategory(id: Int) {
        services.postValue(listOf())
        viewModelScope.launch() {
            try {
                processing.postValue(true)
                val response = repository.getServiceByCategory(id.toString())
                processing.postValue(false)
                if (!response.isSuccessful) {
                    serviceError.postValue("Failed to load data. Error code: ${response.code()}")
                }
                response.body()?.let {
                    services.postValue(it.services)
                }
            } catch (e: Exception) {

            }
        }
    }

    fun getService() {
        repository.getServiceCategory()
//        viewModelScope.launch {
//            Log.i("jiheeSon", "view model scope")
//            withContext(Dispatchers.IO) {
//                //repository.getServiceCategory()
//                Log.i("jiheeSon", "with context")
//                fun1()
//                Log.i("jiheeSon", "with context end")
//            }
//
//            launch(Dispatchers.IO) {
//                Log.i("jiheeSon", "launch")
//                fun2()
//                Log.i("jiheeSon", "launch end")
//            }
//
//            launch(Dispatchers.IO) {
//                Log.i("jiheeSon", "launch2")
//                fun3()
//                Log.i("jiheeSon", "launch2 end")
//            }
//        }
    }

//    private suspend fun fun1() {
//        Log.i("jiheeSon", "in fun1 start")
//        delay(1000)
//        Log.i("jiheeSon", "in fun1 end")
//    }
//
//    private suspend fun fun2() {
//        Log.i("jiheeSon", "in fun2 start")
//        delay(3000)
//        Log.i("jiheeSon", "in fun2 end")
//    }
//
//    private suspend fun fun3() {
//        Log.i("jiheeSon", "in fun3 start")
//        delay(1000)
//        Log.i("jiheeSon", "in fun3 end")
//
//    }

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