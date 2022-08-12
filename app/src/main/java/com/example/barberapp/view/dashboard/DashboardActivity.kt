package com.example.barberapp.view.dashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.barberapp.BuildConfig
import com.example.barberapp.R
import com.example.barberapp.databinding.ActivityDashboardBinding
import com.example.barberapp.model.Repository
import com.example.barberapp.model.remote.ApiService
import com.example.barberapp.view.auth.LoginActivity
import com.example.barberapp.view.appointment.AppointmentActivity
import com.example.barberapp.view.dashboard.fragments.*
import com.example.barberapp.view.history.HistoryActivity
import com.example.barberapp.view.service.ServiceActivity
import com.example.barberapp.viewmodel.DashboardVMFactory
import com.example.barberapp.viewmodel.DashboardViewModel

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var viewModel: DashboardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment, HomeFragment())
            .commit()

        setUpViewModel()
        initNavBar()
        setUpNotificationEvent()

    }

    private fun setUpNotificationEvent() {
        binding.toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.menu_notification) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment, AlertFragment())
                    .addToBackStack(null)
                    .commit()
                true
            } else {
                false
            }
        }
    }

    private fun setUpViewModel() {
        val vmFactory = DashboardVMFactory(Repository(ApiService.getInstance()))
        viewModel = ViewModelProvider(this@DashboardActivity, vmFactory)[DashboardViewModel::class.java]

        val pref = getSharedPreferences("user_info", MODE_PRIVATE)
        val token = pref.getString("api_token", "")
        val userId = pref.getString("user_Id", "")
        viewModel.getDashboard(token!!, userId!!)
    }

    private fun initNavBar() {
        setSupportActionBar(binding.toolbar)

        binding.navDrawer.setNavigationItemSelectedListener { menuItems ->
            binding.layoutDashboard.closeDrawer(GravityCompat.START)
            when (menuItems.itemId) {
                R.id.nav_book -> { startActivity(Intent(this@DashboardActivity, AppointmentActivity::class.java)) }
                R.id.nav_appointments -> { startActivity(Intent(this@DashboardActivity, HistoryActivity::class.java)) }
                R.id.nav_service -> { startActivity(Intent(this@DashboardActivity, ServiceActivity::class.java)) }
                R.id.nav_reach -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment, ReachFragment())
                        .addToBackStack(null)
                        .commit()
                }
                R.id.nav_hours -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment, WorkingHoursFragment())
                        .addToBackStack(null)
                        .commit()
                }
                R.id.nav_share -> {
                    shareApp()
                }
                R.id.nav_about -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment, AboutAppFragment())
                        .addToBackStack(null)
                        .commit()
                }
                R.id.nav_logout -> {
                    logoutUser()
                    startActivity(Intent(this@DashboardActivity, LoginActivity::class.java))
                    finish()
                }
            }
            true
        }
    }

    private fun shareApp() {
        val appId = BuildConfig.APPLICATION_ID
        val text = "https://play.google.com/store/apps/details?id=$appId"
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Share the app!")
        shareIntent.putExtra(Intent.EXTRA_TEXT, text)
        startActivity(shareIntent)
    }

    private fun logoutUser() {
        val pref = getSharedPreferences("user_info", MODE_PRIVATE)
        val editor = pref.edit()
        editor.clear()
        editor.apply()
        //return editor.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_notification, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (binding.layoutDashboard.isDrawerOpen(GravityCompat.START)) {
                binding.layoutDashboard.closeDrawer(GravityCompat.START)
            } else {
                binding.layoutDashboard.openDrawer(GravityCompat.START)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if(binding.layoutDashboard.isDrawerOpen(GravityCompat.START)){
            binding.layoutDashboard.closeDrawers()
        }else{
            super.onBackPressed()
        }
    }

    fun openDrawer() {
        binding.layoutDashboard.openDrawer(GravityCompat.START)
    }
}