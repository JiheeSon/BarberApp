package com.example.barberapp.view.dashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.barberapp.R
import com.example.barberapp.databinding.ActivityDashboardBinding
import com.example.barberapp.model.Repository
import com.example.barberapp.model.remote.ApiService
import com.example.barberapp.view.LoginActivity
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
                    .replace(R.id.fragment, HomeFragment())
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
        viewModel.getDashboard()
    }

    private fun initNavBar() {
        setSupportActionBar(binding.toolbar)

        binding.navDrawer.setNavigationItemSelectedListener { menuItems ->
            binding.layoutDashboard.closeDrawer(GravityCompat.START)
            when (menuItems.itemId) {
                R.id.nav_service -> {Toast.makeText(this, "service", Toast.LENGTH_SHORT).show()}
                R.id.nav_logout -> {
                    Toast.makeText(this, "log out", Toast.LENGTH_SHORT).show()
                    logoutUser()
                    startActivity(Intent(this@DashboardActivity, LoginActivity::class.java))
                    finish()
                }
            }
            true
        }
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
}