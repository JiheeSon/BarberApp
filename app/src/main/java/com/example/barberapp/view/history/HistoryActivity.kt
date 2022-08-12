package com.example.barberapp.view.history

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.barberapp.R
import com.example.barberapp.databinding.ActivityHistoryBinding
import com.example.barberapp.model.Repository
import com.example.barberapp.model.remote.ApiService
import com.example.barberapp.view.appointment.fragments.BarberListFragment
import com.example.barberapp.viewmodel.AppointmentVMFactory
import com.example.barberapp.viewmodel.AppointmentViewModel
import com.example.barberapp.viewmodel.HistoryVMFactory
import com.example.barberapp.viewmodel.HistoryViewModel

class HistoryActivity : AppCompatActivity() {
    private lateinit var viewModel: HistoryViewModel
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        binding.toolbar.title = "Appointments"

        setUpViewModel()
        getUserData()

//        supportFragmentManager.beginTransaction()
//            .replace(R.id.fragment, AppointmentListFragment())
//            .commit()

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.history_fragment) as NavHostFragment? ?: return

        val navController = host.navController
        appBarConfiguration = AppBarConfiguration(setOf(R.id.appointmentListFragment, R.id.appointmentDetailFragment, R.id.rescheduleFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    private fun getUserData() {
        val pref = getSharedPreferences("user_info", AppCompatActivity.MODE_PRIVATE)
        val userId = pref.getString("user_Id", "")
        val token = pref.getString("api_token", "")
        viewModel.apiToken.postValue(token!!)
        viewModel.userId.postValue(userId!!)    }

    private fun setUpViewModel() {
        val vmFactory = HistoryVMFactory(Repository(ApiService.getInstance()))
        viewModel = ViewModelProvider(this@HistoryActivity, vmFactory)[HistoryViewModel::class.java]
    }
}